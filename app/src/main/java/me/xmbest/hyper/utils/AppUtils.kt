package me.xmbest.hyper.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import dalvik.system.DexFile
import me.xmbest.hyper.App
import me.xmbest.hyper.annotations.HookModule
import java.io.IOException

/**
 * 应用工具类
 * @author xmbest
 * @date 2024/09/23
 */
class AppUtils {
    companion object{
        private val sPackageManager: PackageManager = App.sInstance!!.packageManager

        fun getApplicationNameAndIcon( packageName: String): Pair<String, Drawable?>? {
            // 通过包名来获取应用信息
            val applicationInfo: ApplicationInfo? = try {
                sPackageManager.getApplicationInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                // 如果找不到应用，则返回空值
                return null
            }

            return applicationInfo?.let {
                // 获取应用的名字
                val appName = sPackageManager.getApplicationLabel(applicationInfo).toString()

                // 获取应用的图标
                val appIcon = sPackageManager.getApplicationIcon(applicationInfo)

                Pair(appName, appIcon)
            }
        }

        fun getClassesInPackage(packageName: String, context: Context): List<String> {
            val packageNameList = mutableListOf<String>()
            try {
                // 获取应用程序的APK路径
                val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
                val sourceDir = applicationInfo.sourceDir
                val dexFile = DexFile(sourceDir)

                // 遍历dex文件中的所有类
                for (entry in dexFile.entries()) {
                    if (entry.contains(packageName)) {
                        // 尝试加载类
                        try {
                            if (entry.contains("$")) continue
                            val clazz = Class.forName(entry)
                            val annotation = clazz.getAnnotation(HookModule::class.java)
                            annotation?.let {
                                packageNameList.add(annotation.packageName)
                            }
                        } catch (e: ClassNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return packageNameList
        }

    }
}