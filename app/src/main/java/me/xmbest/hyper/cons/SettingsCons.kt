package me.xmbest.hyper.cons

import android.util.Log


/**
 * com.android.settings常量
 * @author xmbest
 * @date 2024/09/13
 */
class SettingsCons {
    companion object {

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

        /**
         * 设备信息键值对
         * device_cpu 处理器
         * device_memory 运行内存
         * device_battery 电池容量
         * device_screen_resolution 分辨率
         * device_screen_size 屏幕尺寸
         * device_miui_version OS版本
         * device_camera 摄像头
         *
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
            Log.d(TAG, "getDeviceInfoMapKey arg1 = $arg1")
            return when {
                arg1.contains("+") && arg1.contains("GB") -> MEMORY
                arg1.contains("mAh") -> BATTERY
                arg1.contains("*") || arg1.contains("x") -> SCREEN_RESOLUTION
                arg1.contains("""(英寸|″)""".toRegex())  -> SCREEN_SIZE
                arg1.contains("""(平台|澎湃|高通|天玑|骁龙)""".toRegex()) -> CPU
                arg1.contains("MPSS") -> BASEBAND
                arg1.contains("MP") -> CAMERA
                arg1.contains("""(Xiaomi|REDMI|XIAOMI|Redmi)""".toRegex()) -> deviceName.second
                arg1.contains("""^\d+\.\d+\.\d+\.\d+\.\S+""".toRegex()) -> MIUI_VERSION
                else -> ""
            }
        }


    }
}