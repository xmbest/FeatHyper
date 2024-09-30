package me.xmbest.hyper.base

import android.util.Log

open class BaseModule {
    protected val TAG:String = javaClass.simpleName

    fun logD(log:String){
        Log.d(TAG, log)
    }
}