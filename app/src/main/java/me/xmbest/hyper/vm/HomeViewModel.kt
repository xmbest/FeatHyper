package me.xmbest.hyper.vm

import me.xmbest.hyper.App
import me.xmbest.hyper.base.BaseViewModel
import me.xmbest.hyper.utils.AppUtils

class HomeViewModel : BaseViewModel() {
    private val _packageNameList: ArrayList<String> = ArrayList()

    fun getPackageNameList(): List<String> {
        return _packageNameList.ifEmpty { getPackageList() }
    }

    private fun getPackageList(): ArrayList<String> {
        _packageNameList.addAll(AppUtils.getClassesInPackage("me.xmbest.hyper.hook.module", App.sInstance))
        return _packageNameList
    }
}