package me.xmbest.hyper

import android.annotation.SuppressLint
import android.app.Application
import me.xmbest.hyper.utils.SPUtils

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var sInstance: Application? = null

        fun getInstance(): Application = sInstance!!
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        init()
    }

    private fun init(){
        SPUtils.getInstance().init(this)
    }

}