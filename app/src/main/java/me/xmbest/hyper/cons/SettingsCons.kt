package me.xmbest.hyper.cons

import android.util.Log
import me.xmbest.hyper.BuildConfig

/**
 * com.android.settings常量
 * @author xmbest
 * @date 2024/09/13
 */
object SettingsCons {

    private const val TAG = "SettingsCons"

    /**
     * 编辑手机信息
     */
    const val EDIT_DEVICE_INFO = "EDIT_DEVICE_INFO"

    /**
     * 手机修改后的名称
     */
    const val EDIT_DEVICE_NAME_VALUE = "com_android_settings_device_name"

    /**
     * 手机设备名称
     */
    val deviceName = Pair(EDIT_DEVICE_NAME_VALUE, "设备名称")

    private const val CPU = "处理器"
    private const val MEMORY = "运行内存"
    private const val BATTERY = "电池容量"
    private const val SCREEN_RESOLUTION = "分辨率"
    private const val SCREEN_SIZE = "屏幕尺寸"
    const val MIUI_VERSION = "OS版本"
    private const val CAMERA = "摄像头"
    private const val BASEBAND = "基带"

    // 预编译正则表达式，避免每次调用 getDeviceInfoMapKey 时重复编译
    private val SCREEN_SIZE_REGEX = Regex("""(英寸|″)""")
    private val CPU_REGEX = Regex("""(平台|澎湃|高通|天玑|骁龙)""")
    private val DEVICE_NAME_REGEX = Regex("""(Xiaomi|REDMI|XIAOMI|Redmi)""")
    private val MIUI_VERSION_REGEX = Regex("""^\d+\.\d+\.\d+\.\d+\.\S+""")

    /**
     * 设备信息键值对
     */
    val deviceInfoMap = mapOf(
        deviceName.second to deviceName.first,
        CPU to "com_android_settings_device_cpu",
        MEMORY to "com_android_settings_device_memory",
        BATTERY to "com_android_settings_device_battery",
        SCREEN_RESOLUTION to "com_android_settings_device_screen_resolution",
        SCREEN_SIZE to "com_android_settings_device_screen_size",
        MIUI_VERSION to "com_android_settings_device_miui_version",
        CAMERA to "com_android_settings_device_camera",
        BASEBAND to "com_android_settings_device_baseband",
    )

    /**
     * 设置设备信息可能直接调用com.android.settings.device.BaseDeviceCardItem#setValue(CharSequence)
     */
    fun getDeviceInfoMapKey(arg1: String): String {
        if (BuildConfig.DEBUG) Log.d(TAG, "getDeviceInfoMapKey arg1 = $arg1")
        return when {
            arg1.contains("+") && arg1.contains("GB") -> MEMORY
            arg1.contains("mAh") -> BATTERY
            arg1.contains("*") || arg1.contains("x") -> SCREEN_RESOLUTION
            SCREEN_SIZE_REGEX.containsMatchIn(arg1) -> SCREEN_SIZE
            CPU_REGEX.containsMatchIn(arg1) -> CPU
            arg1.contains("MPSS") -> BASEBAND
            arg1.contains("MP") -> CAMERA
            DEVICE_NAME_REGEX.containsMatchIn(arg1) -> deviceName.second
            MIUI_VERSION_REGEX.containsMatchIn(arg1) -> MIUI_VERSION
            else -> ""
        }
    }
}
