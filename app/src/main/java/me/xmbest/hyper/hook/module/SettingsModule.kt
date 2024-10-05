package me.xmbest.hyper.hook.module

import android.util.Log
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.xmbest.hyper.annotations.HookMethod
import me.xmbest.hyper.annotations.HookModule
import me.xmbest.hyper.cons.SettingsCons
import me.xmbest.hyper.base.BaseModule
import me.xmbest.hyper.utils.XSPUtils

/**
 * settings 模块
 * @author xmbest
 * @date 2024/09/13
 */

@HookModule("com.android.settings")
class SettingsModule : BaseModule() {
    /**
     * 修改机型名称
     * @param lpParam XC_LoadPackage.LoadPackageParam 提供 classLoader
     * @see <a href="https://www.coolapk.com/feed/55743488">修改手机名称</a>
     * @see <a href="https://www.coolapk.com/feed/55860255">修改处理器名称</a>
     */
    @HookMethod(value = SettingsCons.EDIT_DEVICE_INFO, defaultEnable = false)
    fun editDeviceInfo(lpParam: XC_LoadPackage.LoadPackageParam) {
        logD("editDeviceInfo")
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
                        Log.d(TAG, "getDeviceMarketName afterHookedMethod: ")
                        param?.let {
                            param.result = deviceName
                        }
                    }
                }
            )
        }

        // 设备信息
        val clazz = XposedHelpers.findClass(
            "com.android.settings.device.DeviceCardInfo",
            lpParam.classLoader
        )

        XposedHelpers.findAndHookMethod("com.android.settings.device.BaseDeviceCardItem",
            lpParam.classLoader, "setValue",
            CharSequence::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    Log.d(TAG, "setValue String beforeHookedMethod: ")
                    param?.let {
                        param.args?.let {
                            if (it.isNotEmpty()) {
                                val key =
                                    SettingsCons.getDeviceInfoMapKey(it[0].toString())
                                if (key.isNotEmpty()) {
                                    val value = XSPUtils.getString(
                                        SettingsCons.deviceInfoMap[key],
                                        ""
                                    )
                                    if (value.isNotEmpty()) {
                                        it[0] = value
                                    }
                                }
                            }
                        }
                    }
                }
            })


        XposedHelpers.findAndHookMethod(
            "com.android.settings.device.BaseDeviceCardItem",
            lpParam.classLoader,
            "setValue",
            clazz,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    Log.d(TAG, "setValue DeviceCardInfo beforeHookedMethod: ")
                    param?.let {
                        it.args?.let { arg ->
                            if (arg.isNotEmpty()) {
//                                val setTitle = clazz.getMethod("setTitle", String::class.java)
                                val setValue = clazz.getMethod("setValue", String::class.java)
                                val getTitle = clazz.getMethod("getTitle")
                                val getFirstValue = clazz.getMethod("getFirstValue")
                                val getSecondValue = clazz.getMethod("getSecondValue")
                                val setFirstValue =
                                    clazz.getMethod("setFirstValue", String::class.java)
                                val setSecondValue =
                                    clazz.getMethod("setSecondValue", String::class.java)
                                val getTitle2 = clazz.getMethod("getTitle2")
                                val getValue = clazz.getMethod("getValue")
                                val getKey = clazz.getMethod("getKey")
                                val title = getTitle.invoke(arg[0])
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
                                    val result =
                                        XSPUtils.getString(SettingsCons.deviceInfoMap[title], "")
                                    Log.d(TAG, "result = $result")

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
        )


    }

}