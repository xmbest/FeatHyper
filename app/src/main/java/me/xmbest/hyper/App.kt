package me.xmbest.hyper

import android.annotation.SuppressLint
import android.app.Application

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var sInstance: Application
            private set

        fun getInstance(): Application = sInstance
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }
}