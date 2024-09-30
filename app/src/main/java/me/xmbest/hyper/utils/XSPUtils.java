package me.xmbest.hyper.utils;

import de.robv.android.xposed.XSharedPreferences;

public class XSPUtils {

    public static XSharedPreferences xsp;

    /**
     * 初始化
     *
     * @param packageName 包名
     */
    public static void initXSP(String packageName,String filename) {
        if (xsp == null){
            xsp = new XSharedPreferences(packageName,filename);
        }
    }

    public static boolean getBoolean(String key, boolean def) {
        return xsp.getBoolean(key, def);
    }

    public static String getString(String key, String def) {
        return xsp.getString(key, def);
    }

    public static int getInt(String key, int def) {
        return xsp.getInt(key, def);
    }

    public static float getFloat(String key, float def) {
        return xsp.getFloat(key, def);
    }

    public static long getLong(String key, long def) {
        return xsp.getLong(key, def);
    }
}
