package me.xmbest.hyper.hook

import android.util.Log
import dalvik.system.DexFile
import dalvik.system.PathClassLoader
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.xmbest.hyper.BuildConfig
import me.xmbest.hyper.annotations.HookMethod
import me.xmbest.hyper.annotations.HookModule
import me.xmbest.hyper.utils.SPUtils
import me.xmbest.hyper.utils.XSPUtils

/**
 * hook入口类
 * @author xmbest
 * @date 2024/09/13
 */

class MainHook : IXposedHookZygoteInit, IXposedHookLoadPackage {
    private val TAG: String = "MainHook"

    companion object {
        val mHookClassMap = HashMap<String, Class<*>>()
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        Log.d(TAG, "initZygote")
        initXSharedPreferences()
        startupParam?.let {
            loadHookClass(it)
        }
    }

    override fun handleLoadPackage(lpParam: XC_LoadPackage.LoadPackageParam?) {
        lpParam?.let {
            Log.d(TAG, "handleLoadPackage: packageName = ${lpParam.packageName}")
            if (mHookClassMap.keys.contains(lpParam.packageName)) {
                val clz = mHookClassMap[lpParam.packageName]
                clz?.let {
                    val methods = clz.methods
                    methods.forEach {
                        val annotation = it.getAnnotation(HookMethod::class.java)
                        if (annotation != null) {
                            Log.d(
                                TAG,
                                "annotation.value = ${annotation.value},annotation.defaultEnable = ${annotation.defaultEnable}"
                            )
                            val instance = clz.getDeclaredConstructor().newInstance()
                            if (XSPUtils.getBoolean(annotation.value,annotation.defaultEnable)){
                                it.invoke(instance, lpParam)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化XSharedPreferences
     */
    private fun initXSharedPreferences(){
        XSPUtils.initXSP(BuildConfig.APPLICATION_ID,
            SPUtils.mPrefsName)
    }

    /**
     * 加载hook类
     * @param startupParam IXposedHookZygoteInit.StartupParam
     */
    private fun loadHookClass(startupParam: IXposedHookZygoteInit.StartupParam) {
        if (mHookClassMap.isEmpty()) {
            val pathClassLoader =
                PathClassLoader(startupParam.modulePath, ClassLoader.getSystemClassLoader())
            val pathList = XposedHelpers.getObjectField(pathClassLoader, "pathList")
            val dexElements = XposedHelpers.getObjectField(pathList, "dexElements") as Array<*>
            var dexFile: DexFile? = null
            for (element in dexElements) {
                dexFile = XposedHelpers.getObjectField(element, "dexFile") as DexFile
            }
            if (dexFile != null) {
                val enumeration = dexFile.entries()
                while (enumeration.hasMoreElements()) {
                    val className = enumeration.nextElement()
                    if (!className.contains("$") && className.contains("me.xmbest.hyper.hook.module")) {
                        val cls = Class.forName(className)
                        val annotation = cls.getAnnotation(HookModule::class.java)
                        annotation?.let {
                            mHookClassMap[it.packageName] = cls
                        }
                    }
                }
            }
            Log.d(TAG, "mHookClassMap = $mHookClassMap")
        }
    }

}