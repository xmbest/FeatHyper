package me.xmbest.hyper.utils

import android.content.Context
import me.xmbest.hyper.App

object ResUtils {
    @JvmStatic
    fun getContext(): Context = App.getInstance()

    fun getString(rid: Int) = getContext().resources.getString(rid)
}
