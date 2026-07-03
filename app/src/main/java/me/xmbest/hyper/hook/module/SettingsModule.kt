package me.xmbest.hyper.hook.module

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.xmbest.hyper.annotations.HookMethod
import me.xmbest.hyper.annotations.HookModule
import me.xmbest.hyper.base.BaseModule
import me.xmbest.hyper.cons.SettingsCons
import me.xmbest.hyper.utils.XSPUtils

/**
 * settings 模块
 * @author xmbest
 * @date 2024/09/13
 */

@HookModule("com.android.settings")
class SettingsModule : BaseModule() {

    override val TAG = "SettingsModule"

    companion object {
        private const val CLASS_MIUI_ABOUT_PHONE = "com.android.settings.device.MiuiAboutPhoneUtils"
        private const val CLASS_BASE_DEVICE_CARD_ITEM = "com.android.settings.device.BaseDeviceCardItem"
        private const val CLASS_DEVICE_CARD_INFO = "com.android.settings.device.DeviceCardInfo"
    }

    /**
     * 修改机型名称
     * @param lpParam XC_LoadPackage.LoadPackageParam 提供 classLoader
     */
    @HookMethod(value = SettingsCons.EDIT_DEVICE_INFO, defaultEnable = false)
    fun editDeviceInfo(lpParam: XC_LoadPackage.LoadPackageParam) {
        setSystemPhoneName(lpParam)
        setSystemVersion(lpParam)
        setSettingsInfo(lpParam)
    }

    /**
     * 设置设备属性信息
     */
    private fun setSettingsInfo(lpParam: XC_LoadPackage.LoadPackageParam) {
        setSettingsAfter200Version(lpParam)
        setSettingsBefore200Version(lpParam)
    }

    /**
     * 设置设备版本号
     */
    private fun setSystemVersion(param: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            CLASS_MIUI_ABOUT_PHONE,
            param.classLoader,
            "getOsVersionCode",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    logD("MiuiAboutPhoneUtils.getOsVersionCode.afterHookedMethod")
                    XSPUtils.getString(SettingsCons.deviceInfoMap[SettingsCons.MIUI_VERSION], "")
                        .let {
                            if (it.isNotEmpty()) param?.result = it
                        }
                }
            })
    }

    /**
     * 加载手机名称并且设置
     */
    private fun setSystemPhoneName(lpParam: XC_LoadPackage.LoadPackageParam) {
        val deviceName = XSPUtils.getString(SettingsCons.EDIT_DEVICE_NAME_VALUE, "")
        logD("deviceName = $deviceName")
        if (deviceName.isNotEmpty()) {
            XposedHelpers.findAndHookMethod(
                CLASS_MIUI_ABOUT_PHONE,
                lpParam.classLoader,
                "getDeviceMarketName",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        super.afterHookedMethod(param)
                        logD("getDeviceMarketName afterHookedMethod")
                        param?.result = deviceName
                    }
                })
        }
    }

    /**
     * 澎湃 2.0.200之后
     */
    private fun setSettingsAfter200Version(lpParam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            CLASS_BASE_DEVICE_CARD_ITEM,
            lpParam.classLoader,
            "setValue",
            CharSequence::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    logD("BaseDeviceCardItem.setValue String beforeHookedMethod")
                    param?.args?.let { args ->
                        if (args.isNotEmpty()) {
                            val key = SettingsCons.getDeviceInfoMapKey(args[0].toString())
                            if (key.isNotEmpty()) {
                                val value = XSPUtils.getString(
                                    SettingsCons.deviceInfoMap[key], ""
                                )
                                if (value.isNotEmpty()) {
                                    args[0] = value
                                }
                                logD("key = $key , value = $value")
                            }
                        }
                    }
                }
            })
    }

    /**
     * 澎湃 2.0.200前
     */
    private fun setSettingsBefore200Version(lpParam: XC_LoadPackage.LoadPackageParam) {
        val clazz = XposedHelpers.findClass(CLASS_DEVICE_CARD_INFO, lpParam.classLoader)
        val baseDeviceCardItem = XposedHelpers.findClass(CLASS_BASE_DEVICE_CARD_ITEM, lpParam.classLoader)

        // 新版本移除了，会抛异常
        runCatching {
            XposedHelpers.findAndHookMethod(
                CLASS_BASE_DEVICE_CARD_ITEM,
                lpParam.classLoader,
                "setValue",
                clazz,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam?) {
                        super.beforeHookedMethod(param)
                        logD("DeviceInfoAdapter.setDataList before")
                        updateDeviceInfo(clazz, param)
                    }
                })
        }.onFailure {
            logE("setSettingsBefore200Version setValue hook failed", it)
        }

        // 旧版本 BaseDeviceCardItem.setValue(BaseDeviceCardItem) -> 新版本 BaseDeviceCardItem.setValue(BaseDeviceCardItem,boolean)
        baseDeviceCardItem.methods.firstOrNull {
            it.name == "setValue" && it.parameterCount == 2
        }?.let { method ->
            XposedBridge.hookMethod(method, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    logD("XposedBridge.hookMethod")
                    updateDeviceInfo(clazz, param)
                }
            })
        }
    }

    /**
     * 更新设备信息
     */
    private fun updateDeviceInfo(clazz: Class<*>, param: XC_MethodHook.MethodHookParam?) {
        param?.let { hookParam ->
            hookParam.args?.let { arg ->
                if (arg.isEmpty()) return

                runCatching {
                    val setValue = clazz.getMethod("setValue", String::class.java)
                    val getTitle = clazz.getMethod("getTitle")
                    val getFirstValue = clazz.getMethod("getFirstValue")
                    val getSecondValue = clazz.getMethod("getSecondValue")
                    val setFirstValue = clazz.getMethod("setFirstValue", String::class.java)
                    val setSecondValue = clazz.getMethod("setSecondValue", String::class.java)

                    val title = (getTitle.invoke(arg[0]) as String).trim()
                    val firstValue = getFirstValue.invoke(arg[0])

                    if (SettingsCons.deviceInfoMap.keys.contains(title)) {
                        val result = XSPUtils.getString(SettingsCons.deviceInfoMap[title], "")
                        logD("result = $result")

                        if (result.isNotBlank()) {
                            setValue.invoke(arg[0], result)
                        }

                        firstValue?.let {
                            setFirstValue.invoke(arg[0], result)
                            setSecondValue.invoke(arg[0], "")
                        }
                    }
                }.onFailure {
                    logE("updateDeviceInfo failed", it)
                }
            }
        }
    }
}
