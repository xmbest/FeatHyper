package me.xmbest.hyper.cons


/**
 * com.android.settings常量
 * @author xmbest
 * @date 2024/09/13
 */
class SettingsCons {
    companion object {

        /**
         * 编辑手机信息
         */
        const val EDIT_DEVICE_INFO = "EDIT_DEVICE_INFO"

        /**
         * 手机修改后的名称
         */
        const val EDIT_DEVICE_NAME_VALUE = "com_android_settings_device_name"

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
            "处理器" to "com_android_settings_device_cpu",
            "运行内存" to "com_android_settings_device_memory",
            "电池容量" to "com_android_settings_device_battery",
            "分辨率" to "com_android_settings_device_screen_resolution",
            "屏幕尺寸" to "com_android_settings_device_screen_size",
            "OS版本" to "com_android_settings_device_miui_version",
            "摄像头" to "com_android_settings_device_camera",
        )

        /**
         * 手机设备名称
         */
        val deviceName = Pair(EDIT_DEVICE_NAME_VALUE,"设备名称")


    }
}