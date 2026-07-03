package me.xmbest.hyper.base

import android.util.Log
import me.xmbest.hyper.BuildConfig

open class BaseModule {

    protected open val TAG: String = javaClass.simpleName

    fun logD(log: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, log)
        }
    }

    fun logE(log: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            if (throwable != null) {
                Log.e(TAG, log, throwable)
            } else {
                Log.e(TAG, log)
            }
        }
    }
}