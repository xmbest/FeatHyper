package me.xmbest.hyper.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import dalvik.system.DexFile
import me.xmbest.hyper.App
import me.xmbest.hyper.BuildConfig
import me.xmbest.hyper.annotations.HookModule

/**
 * 应用工具类
 * @author xmbest
 * @date 2024/09/23
 */
class AppUtils {
    companion object {
        private const val TAG = "AppUtils"

        private val sPackageManager: PackageManager by lazy {
            App.sInstance.packageManager
        }

        fun getApplicationNameAndIcon(packageName: String): Pair<String, Drawable?>? {
            val applicationInfo: ApplicationInfo? = try {
                sPackageManager.getApplicationInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                return null
            }

            return applicationInfo?.let {
                val appName = sPackageManager.getApplicationLabel(applicationInfo).toString()
                val appIcon = sPackageManager.getApplicationIcon(applicationInfo)
                Pair(appName, appIcon)
            }
        }

        fun getClassesInPackage(packageName: String, context: Context): List<String> {
            val packageNameList = mutableListOf<String>()
            var dexFile: DexFile? = null
            try {
                val applicationInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
                val sourceDir = applicationInfo.sourceDir
                dexFile = DexFile(sourceDir)
                for (entry in dexFile.entries()) {
                    if (entry.contains(packageName) && !entry.contains("$")) {
                        runCatching {
                            val clazz = Class.forName(entry)
                            val annotation = clazz.getAnnotation(HookModule::class.java)
                            annotation?.let {
                                packageNameList.add(it.packageName)
                            }
                        }.onFailure { e ->
                            if (BuildConfig.DEBUG) Log.e(TAG, "Failed to load class: $entry", e)
                        }
                    }
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) Log.e(TAG, "Failed to read classes from package", e)
            } finally {
                runCatching { dexFile?.close() }
            }
            return packageNameList
        }
    }
}
