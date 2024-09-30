package me.xmbest.hyper.utils

import android.content.Context
import me.xmbest.hyper.App

object ResUtils {
    @JvmStatic
    fun getInstance():Context = App.getInstance()

    fun getString(rid:Int) = getInstance().resources.getString(rid)
}