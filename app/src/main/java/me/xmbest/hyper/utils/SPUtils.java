package me.xmbest.hyper.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class SPUtils {

    private final String TAG = "SPUtils";
    public static SPUtils xsp;
    public SharedPreferences sp;

    public static String mPrefsName = "hyper_prefs";

    private SPUtils() {

    }

    public static synchronized SPUtils getInstance() {
        if (xsp == null) {
            xsp = new SPUtils();
        }
        return xsp;
    }

    /**
     * 初始化
     *
     * @param context
     */
    @SuppressLint("WorldReadableFiles")
    public boolean init(Context context) {
        try {
            Log.d(TAG,"set Context.MODE_WORLD_READABLE");
            // MODE_WORLD_READABLE 过时了,如果没有在xposed中被启用会crash
            sp = context.getSharedPreferences(mPrefsName, Context.MODE_WORLD_READABLE);
            return true;
        }catch (Exception e){
            Log.d(TAG,"e.message = " + e.getMessage());
//            sp = context.getSharedPreferences(mPrefsName,Context.MODE_PRIVATE);
            return false;
        }
    }

    /**
     * 下面的是读取数据
     *
     * @param key
     * @param def
     * @return
     */
    public static String getString(String key, String def) {
        return SPUtils.getInstance().sp.getString(key, def);
    }

    public static int getInt(String key, int def) {
        return SPUtils.getInstance().sp.getInt(key, def);
    }

    public static float getFloat(String key, float def) {
        return SPUtils.getInstance().sp.getFloat(key, def);
    }

    public static long getLong(String key, long def) {
        return SPUtils.getInstance().sp.getLong(key, def);
    }

    public static boolean getBoolean(String key, boolean def) {
        return SPUtils.getInstance().sp.getBoolean(key, def);
    }

    /**
     * 下面是保存数据
     *
     * @param key
     * @param v
     * @return
     */
    public static boolean setString(String key, String v) {
        return SPUtils.getInstance().sp.edit().putString(key, v).commit();
    }

    public static boolean setInt(String key, int v) {
        return SPUtils.getInstance().sp.edit().putInt(key, v).commit();
    }

    public static boolean setBoolean(String key, boolean v) {
        return SPUtils.getInstance().sp.edit().putBoolean(key, v).commit();
    }

    public static boolean setFloat(String key, float v) {
        return SPUtils.getInstance().sp.edit().putFloat(key, v).commit();
    }

    public static boolean setLong(String key, long v) {
        return SPUtils.getInstance().sp.edit().putLong(key, v).commit();
    }


}

