package me.xmbest.hyper.utils

import de.robv.android.xposed.XSharedPreferences

/**
 * XSharedPreferences 工具类
 * 用于 Xposed 模块侧跨进程读取 SharedPreferences
 */
object XSPUtils {
    private var xsp: XSharedPreferences? = null

    /**
     * 初始化
     * @param packageName 包名
     * @param filename SharedPreferences 文件名
     */
    fun initXSP(packageName: String, filename: String) {
        if (xsp == null) {
            xsp = XSharedPreferences(packageName, filename)
        }
    }

    fun getBoolean(key: String?, def: Boolean): Boolean {
        return xsp?.getBoolean(key, def) ?: def
    }

    fun getString(key: String?, def: String): String {
        return xsp?.getString(key, def) ?: def
    }

    fun getInt(key: String?, def: Int): Int {
        return xsp?.getInt(key, def) ?: def
    }

    fun getFloat(key: String?, def: Float): Float {
        return xsp?.getFloat(key, def) ?: def
    }

    fun getLong(key: String?, def: Long): Long {
        return xsp?.getLong(key, def) ?: def
    }
}
