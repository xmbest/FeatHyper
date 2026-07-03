package me.xmbest.hyper.vm

import androidx.compose.runtime.mutableStateOf
import me.xmbest.hyper.base.BaseViewModel
import me.xmbest.hyper.cons.SystemUiCons
import me.xmbest.hyper.utils.SPUtils

class SystemuiLockViewModel : BaseViewModel() {
    val enableLockShowSimName = mutableStateOf(SPUtils.getBoolean(SystemUiCons.LOCK_SHOW_SIM_NAME, false))

    fun updateLockShowSimName(value: Boolean) {
        enableLockShowSimName.value = value
        SPUtils.setBoolean(SystemUiCons.LOCK_SHOW_SIM_NAME, value)
    }
}