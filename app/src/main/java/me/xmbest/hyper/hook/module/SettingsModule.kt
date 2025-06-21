package me.xmbest.hyper.hook.module

import android.util.Log
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

    /**
     * 修改机型名称
     * @param lpParam XC_LoadPackage.LoadPackageParam 提供 classLoader
     * @see <a href="https://www.coolapk.com/feed/55743488">修改手机名称</a>
     * @see <a href="https://www.coolapk.com/feed/55860255">修改处理器名称</a>
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
        // OS版本
        XposedHelpers.findAndHookMethod(
            "com.android.settings.device.MiuiAboutPhoneUtils",
            param.classLoader,
            "getOsVersionCode",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    logD("MiuiAboutPhoneUtils.getOsVersionCode.afterHookedMethod")
                    super.afterHookedMethod(param)
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
        // 手机名称
        val deviceName = XSPUtils.getString(SettingsCons.EDIT_DEVICE_NAME_VALUE, "")
        logD("deviceName = $deviceName")
        if (deviceName.isNotEmpty()) {
            XposedHelpers.findAndHookMethod(
                "com.android.settings.device.MiuiAboutPhoneUtils",
                lpParam.classLoader,
                "getDeviceMarketName",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        super.afterHookedMethod(param)
                        logD("getDeviceMarketName afterHookedMethod: ")
                        param?.let {
                            param.result = deviceName
                        }
                    }
                })
        }
    }

    /**
     * 澎湃 2.0.200之后
     */
    private fun setSettingsAfter200Version(lpParam: XC_LoadPackage.LoadPackageParam) {
        // 200后版本
        XposedHelpers.findAndHookMethod(
            "com.android.settings.device.BaseDeviceCardItem",
            lpParam.classLoader,
            "setValue",
            CharSequence::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    logD("BaseDeviceCardItem.setValue String beforeHookedMethod: ")
                    param?.let {
                        param.args?.let {
                            if (it.isNotEmpty()) {
                                val key = SettingsCons.getDeviceInfoMapKey(it[0].toString())
                                if (key.isNotEmpty()) {
                                    val value = XSPUtils.getString(
                                        SettingsCons.deviceInfoMap[key], ""
                                    )
                                    if (value.isNotEmpty()) {
                                        it[0] = value
                                    }
                                    logD("key = $key , value = $value")
                                }
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
        // 设备信息
        val clazz = XposedHelpers.findClass(
            "com.android.settings.device.DeviceCardInfo", lpParam.classLoader
        )

        val baseDeviceCardItem = XposedHelpers.findClass(
            "com.android.settings.device.BaseDeviceCardItem", lpParam.classLoader
        )
        // 新版本移除了，会抛异常
        runCatching {
            XposedHelpers.findAndHookMethod(
                "com.android.settings.device.BaseDeviceCardItem",
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
            logE(it.stackTrace.toString())
        }


        //上面 BaseDeviceCardItem.setValue(BaseDeviceCardItem)是旧版本 -> 更新成BaseDeviceCardItem.setValue(BaseDeviceCardItem,boolean)，保留两者兼容新旧机器
        baseDeviceCardItem.methods.firstOrNull {
//            logD("name = ${it.name},count = ${it.parameterCount}")
            it.name == "setValue" && it.parameterCount == 2
        }?.let {
            XposedBridge.hookMethod(it, object : XC_MethodHook() {
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
     * @param clazz
     * @param param
     */
    private fun updateDeviceInfo(clazz: Class<*>, param: XC_MethodHook.MethodHookParam?) {
        param?.let {
            it.args?.let { arg ->
                if (arg.isNotEmpty()) {
//                                val setTitle = clazz.getMethod("setTitle", String::class.java)
                    val setValue = clazz.getMethod("setValue", String::class.java)
                    val getTitle = clazz.getMethod("getTitle")
                    val getFirstValue = clazz.getMethod("getFirstValue")
                    val getSecondValue = clazz.getMethod("getSecondValue")
                    val setFirstValue = clazz.getMethod("setFirstValue", String::class.java)
                    val setSecondValue = clazz.getMethod("setSecondValue", String::class.java)
                    val getTitle2 = clazz.getMethod("getTitle2")
                    val getValue = clazz.getMethod("getValue")
                    val getKey = clazz.getMethod("getKey")
                    val title = (getTitle.invoke(arg[0]) as String).trim()
                    val firstValue = getFirstValue.invoke(arg[0])
                    val secondValue = getSecondValue.invoke(arg[0])
                    val title2 = getTitle2.invoke(arg[0])
                    val value = getValue.invoke(arg[0])
                    val key = getKey.invoke(arg[0])
                    Log.d(
                        TAG,
                        "key = $key title = $title title2 = $title2 value = $value firstValue = $firstValue secondValue = $secondValue"
                    )
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
                }
            }
        }
    }

}