package me.xmbest.hyper.vm

import androidx.compose.runtime.mutableStateOf
import me.xmbest.hyper.base.BaseViewModel
import me.xmbest.hyper.cons.SystemUiCons
import me.xmbest.hyper.utils.SPUtils

class SystemuiMaterialViewModel : BaseViewModel() {
    val enableForceSoftLightGlass = mutableStateOf(SPUtils.getBoolean(SystemUiCons.FORCE_SOFT_LIGHT_GLASS, false))

    fun updateForceSoftLightGlass(value: Boolean) {
        enableForceSoftLightGlass.value = value
        SPUtils.setBoolean(SystemUiCons.FORCE_SOFT_LIGHT_GLASS, value)
    }
}
