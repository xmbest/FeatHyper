package me.xmbest.hyper.vm

import androidx.compose.runtime.mutableStateOf
import me.xmbest.hyper.cons.SettingsCons
import me.xmbest.hyper.base.BaseViewModel
import me.xmbest.hyper.utils.SPUtils

class SettingsDeviceInfoViewModel : BaseViewModel() {

    /**
     * 是否开启
     */
    val enable = mutableStateOf(SPUtils.getBoolean(SettingsCons.EDIT_DEVICE_INFO, false))

    /**
     * 更新开关状态
     */
    fun updateDeviceEditState(value: Boolean) {
        enable.value = value
        SPUtils.setBoolean(SettingsCons.EDIT_DEVICE_INFO, value)
    }
}