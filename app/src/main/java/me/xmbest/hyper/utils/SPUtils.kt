package me.xmbest.hyper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import me.xmbest.hyper.BuildConfig

/**
 * SharedPreferences 工具类
 * 使用 MODE_WORLD_READABLE 以支持 Xposed 跨进程读取
 */
object SPUtils {
    private const val TAG = "SPUtils"
    const val mPrefsName = "hyper_prefs"

    private var sp: SharedPreferences? = null

    /**
     * 初始化
     * @return true 初始化成功，false 初始化失败
     */
    @SuppressLint("WorldReadableFiles")
    fun init(context: Context): Boolean {
        return try {
            if (BuildConfig.DEBUG) Log.d(TAG, "set Context.MODE_WORLD_READABLE")
            sp = context.getSharedPreferences(mPrefsName, Context.MODE_WORLD_READABLE)
            true
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) Log.d(TAG, "init failed: ${e.message}")
            false
        }
    }

    fun getString(key: String?, def: String): String {
        return sp?.getString(key, def) ?: def
    }

    fun getInt(key: String?, def: Int): Int {
        return sp?.getInt(key, def) ?: def
    }

    fun getFloat(key: String?, def: Float): Float {
        return sp?.getFloat(key, def) ?: def
    }

    fun getLong(key: String?, def: Long): Long {
        return sp?.getLong(key, def) ?: def
    }

    fun getBoolean(key: String?, def: Boolean): Boolean {
        return sp?.getBoolean(key, def) ?: def
    }

    fun setString(key: String?, v: String) {
        sp?.edit()?.putString(key, v)?.apply()
    }

    fun setInt(key: String?, v: Int) {
        sp?.edit()?.putInt(key, v)?.apply()
    }

    fun setBoolean(key: String?, v: Boolean) {
        sp?.edit()?.putBoolean(key, v)?.apply()
    }

    fun setFloat(key: String?, v: Float) {
        sp?.edit()?.putFloat(key, v)?.apply()
    }

    fun setLong(key: String?, v: Long) {
        sp?.edit()?.putLong(key, v)?.apply()
    }
}
