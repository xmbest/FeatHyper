package me.xmbest.hyper.vm

import androidx.compose.runtime.mutableStateOf
import me.xmbest.hyper.cons.SettingsCons
import me.xmbest.hyper.base.BaseViewModule
import me.xmbest.hyper.utils.SPUtils

class SettingsDeviceInfoViewModule : BaseViewModule() {


    /**
     * 设备名称
     */
    val deviceName = mutableStateOf(SPUtils.getString(SettingsCons.deviceName.first,""))

    /**
     * 是否开启
     */
    val open = mutableStateOf(SPUtils.getBoolean(SettingsCons.EDIT_DEVICE_INFO,false))

    /**
     * 更新设备名称
     */
    fun updateDeviceNameValue(value:String){
        deviceName.value = value
        SPUtils.setString(SettingsCons.deviceName.first,value)
    }

    /**
     * 更新开关状态
     */
    fun updateDeviceEditState(value: Boolean){
        open.value = value
        SPUtils.setBoolean(SettingsCons.EDIT_DEVICE_INFO,value)
    }
}