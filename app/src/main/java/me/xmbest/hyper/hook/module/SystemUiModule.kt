package me.xmbest.hyper.hook.module

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.xmbest.hyper.annotations.HookMethod
import me.xmbest.hyper.annotations.HookModule
import me.xmbest.hyper.cons.SystemUiCons
import me.xmbest.hyper.base.BaseModule

/**
 * systemui 模块
 * @author xmbest
 * @date 2024/09/13
 */
@HookModule("com.android.systemui")
class SystemUiModule : BaseModule() {

    override val TAG = "SystemUiModule"

    /**
     * 显示锁屏运营商名称
     * @param lpParam XC_LoadPackage.LoadPackageParam 提供 classLoader
     */
    @SuppressLint("DiscouragedApi")
    @HookMethod(SystemUiCons.LOCK_SHOW_SIM_NAME, false)
    fun showLockSimCardName(lpParam: XC_LoadPackage.LoadPackageParam) {
        logD("showLockSimCardName")
        XposedHelpers.findAndHookMethod(
            "com.android.systemui.statusbar.phone.KeyguardStatusBarView",
            lpParam.classLoader,
            "onFinishInflate",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    logD("afterHookedMethod")
                    param?.let {
                        val view = param.thisObject as View
                        val labelResId: Int = view.resources
                            .getIdentifier("keyguard_carrier_text", "id", "com.android.systemui")
                        val tv = view.findViewById<TextView>(labelResId) ?: run {
                            logE("keyguard_carrier_text not found")
                            return
                        }
                        logD("tv.text = ${tv.text}")
                        if (tv.text.contains("|")) {
                            tv.text = tv.text.split("|")[0]
                        }
                        tv.visibility = View.VISIBLE
                    }
                }
            }
        )
    }

    /**
     * 强制启用柔光玻璃
     * Hook MiuiMaterialUtils.onDefaultThemeChanged，强制将 isDefaultTheme 设为 true
     * @param lpParam XC_LoadPackage.LoadPackageParam 提供 classLoader
     */
    @HookMethod(SystemUiCons.FORCE_SOFT_LIGHT_GLASS, false)
    fun forceSoftLightGlass(lpParam: XC_LoadPackage.LoadPackageParam) {
        logD("forceSoftLightGlass")
        val materialTypeClass = XposedHelpers.findClass(
            "com.miui.systemui.material.MaterialType",
            lpParam.classLoader
        )
        val blurEnum = XposedHelpers.getStaticObjectField(materialTypeClass, "BLUR")
        val glassEnum = XposedHelpers.getStaticObjectField(materialTypeClass, "GLASS")

        // Hook 1: 通知栏材质状态 — BLUR → GLASS
        XposedHelpers.findAndHookMethod(
            "com.android.systemui.statusbar.notification.style.domain" +
                    ".NotificationMaterialStateInteractor\$materialTypeState\$1",
            lpParam.classLoader,
            "invokeSuspend",
            Object::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    if (param?.result == blurEnum) {
                        param?.result = glassEnum
                    }
                }
            }
        )

        // Hook 2: 控制中心容器背景玻璃 — 强制 isBionicsEnabled = true
        XposedHelpers.findAndHookMethod(
            "com.miui.systemui.shade.blur" +
                    ".ShadeBlendBlurController\$isBionicsEnabled\$1",
            lpParam.classLoader,
            "invokeSuspend",
            Object::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    val materialMode = XposedHelpers.getObjectField(param?.thisObject, "L\$0")
                    val bionicsInstance = XposedHelpers.getStaticObjectField(
                        XposedHelpers.findClass(
                            "com.miui.interfaces.controlcenter.data.repository.MaterialMode\$Bionics",
                            lpParam.classLoader
                        ),
                        "INSTANCE"
                    )
                    if (materialMode == bionicsInstance) {
                        param?.result = true
                    }
                }
            }
        )

        // Hook 3: 控制中心插件 — 磁贴 + 音量 + 亮度 玻璃效果
        var pluginHooked = false
        XposedHelpers.findAndHookMethod(
            "com.miui.systemui.controlcenter.container.ControlCenterContentController",
            lpParam.classLoader,
            "onPluginLoaded",
            "com.android.systemui.plugins.Plugin",
            Context::class.java,
            "com.android.systemui.plugins.PluginLifecycleManager",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    if (pluginHooked) return
                    val plugin = param?.args?.get(0) ?: return
                    val pluginClassLoader = plugin.javaClass.classLoader ?: return
                    pluginHooked = true

                    try {
                        // 3a. 跳过默认主题检查（磁贴 + 音量 + 亮度 入口）
                        XposedHelpers.findAndHookMethod(
                            "miui.systemui.util.MiBlurCompat",
                            pluginClassLoader,
                            "getBackgroundMaterialOpenedInDefaultTheme",
                            Context::class.java,
                            object : XC_MethodHook() {
                                override fun afterHookedMethod(param: MethodHookParam?) {
                                    param?.result = true
                                }
                            }
                        )

                        // 3b. 强制 Bionics 模式（音量/亮度的 setMiBackgroundStyle 需要）
                        XposedHelpers.findAndHookMethod(
                            "miui.systemui.util.MiBackgroundStyle",
                            pluginClassLoader,
                            "getMaterialMode",
                            object : XC_MethodHook() {
                                override fun afterHookedMethod(param: MethodHookParam?) {
                                    val bionicsMode = XposedHelpers.findClass(
                                        "miui.systemui.util.MaterialMode\$Bionics",
                                        pluginClassLoader
                                    )
                                    param?.result = XposedHelpers.getStaticObjectField(
                                        bionicsMode, "INSTANCE"
                                    )
                                }
                            }
                        )

                        // 3c. 延迟刷新所有控件（等插件初始化完成）
                        val content = XposedHelpers.getObjectField(param.thisObject, "content")
                        val rootView = XposedHelpers.callMethod(content, "getView") as? View
                        rootView?.postDelayed({
                            refreshAllControls(rootView)
                        }, 500)

                        // 关开柔光玻璃：先写入 0（关闭），再写入 1（开启），触发系统重新加载材质效果
                        val ctx = param.args[1] as Context
                        val handler = Handler(Looper.getMainLooper())
                        try {
                            Settings.Secure.putInt(ctx.contentResolver, "background_blur_enable", 0)
                            logD("forceSoftLightGlass: background_blur_enable -> 0")
                        } catch (e: Throwable) {
                            logD("forceSoftLightGlass write 0 failed: ${e.message}")
                        }
                        handler.postDelayed({
                            try {
                                Settings.Secure.putInt(
                                    ctx.contentResolver,
                                    "background_blur_enable",
                                    1
                                )
                                logD("forceSoftLightGlass: background_blur_enable -> 1")
                            } catch (e: Throwable) {
                                logD("forceSoftLightGlass write 1 failed: ${e.message}")
                            }
                        }, 500)

                        logD("forceTileGlass: all plugin hooks applied")
                    } catch (e: Throwable) {
                        logD("forceTileGlass failed: ${e.message}")
                    }

                }
            }
        )
    }

    /**
     * 递归刷新控制中心所有控件的材质效果
     */
    private fun refreshAllControls(view: View) {
        val className = view.javaClass.name
        when {
            // 磁贴
            className == "miui.systemui.controlcenter.qs.tileview.QSCardItemView" -> {
                try {
                    val state = XposedHelpers.getObjectField(view, "state")
                    val connected = XposedHelpers.getBooleanField(view, "connected")
                    if (state != null) {
                        XposedHelpers.callMethod(view, "updateState", state, connected, true)
                    }
                } catch (_: Throwable) {
                }
            }
            // 音量/亮度滑块
            className.contains("ToggleSlider") -> {
                try {
                    XposedHelpers.callMethod(view, "updateBlendBlur", true)
                } catch (_: Throwable) {
                    try {
                        val config = view.resources.configuration
                        XposedHelpers.callMethod(view, "onConfigurationChanged", config)
                    } catch (_: Throwable) {
                    }
                }
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                refreshAllControls(view.getChildAt(i))
            }
        }
    }
}

/**
 * 递归刷新控制中心所有控件的材质效果
 */
private fun refreshAllControls(view: View) {
    val className = view.javaClass.name
    when {
        // 磁贴
        className == "miui.systemui.controlcenter.qs.tileview.QSCardItemView" -> {
            try {
                val state = XposedHelpers.getObjectField(view, "state")
                val connected = XposedHelpers.getBooleanField(view, "connected")
                if (state != null) {
                    XposedHelpers.callMethod(view, "updateState", state, connected, true)
                }
            } catch (_: Throwable) {
            }
        }
        // 音量/亮度滑块
        className.contains("ToggleSlider") -> {
            try {
                XposedHelpers.callMethod(view, "updateBlendBlur", true)
            } catch (_: Throwable) {
                try {
                    val config = view.resources.configuration
                    XposedHelpers.callMethod(view, "onConfigurationChanged", config)
                } catch (_: Throwable) {
                }
            }
        }
    }
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            refreshAllControls(view.getChildAt(i))
        }
    }
}
