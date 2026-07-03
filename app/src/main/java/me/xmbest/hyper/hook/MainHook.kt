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
import java.util.concurrent.ConcurrentHashMap

/**
 * hook入口类
 * @author xmbest
 * @date 2024/09/13
 */
class MainHook : IXposedHookZygoteInit, IXposedHookLoadPackage {
    private val TAG: String = "MainHook"

    companion object {
        private const val HOOK_MODULE_PACKAGE = "me.xmbest.hyper.hook.module"
        val mHookClassMap = ConcurrentHashMap<String, Class<*>>()
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        if (BuildConfig.DEBUG) Log.d(TAG, "initZygote")
        initXSharedPreferences()
        startupParam?.let {
            loadHookClass(it)
        }
    }

    override fun handleLoadPackage(lpParam: XC_LoadPackage.LoadPackageParam?) {
        lpParam?.let { loadPkg ->
            if (BuildConfig.DEBUG) Log.d(TAG, "handleLoadPackage: packageName = ${loadPkg.packageName}")
            val clz = mHookClassMap[loadPkg.packageName] ?: return
            clz.methods.forEach { method ->
                val annotation = method.getAnnotation(HookMethod::class.java) ?: return@forEach
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "annotation.value = ${annotation.value}, annotation.defaultEnable = ${annotation.defaultEnable}")
                }
                if (XSPUtils.getBoolean(annotation.value, annotation.defaultEnable)) {
                    runCatching {
                        val instance = clz.getDeclaredConstructor().newInstance()
                        method.invoke(instance, loadPkg)
                    }.onFailure {
                        if (BuildConfig.DEBUG) Log.e(TAG, "Failed to invoke hook: ${method.name}", it)
                    }
                }
            }
        }
    }

    /**
     * 初始化XSharedPreferences
     */
    private fun initXSharedPreferences() {
        XSPUtils.initXSP(BuildConfig.APPLICATION_ID, SPUtils.mPrefsName)
    }

    /**
     * 加载hook类
     * @param startupParam IXposedHookZygoteInit.StartupParam
     */
    private fun loadHookClass(startupParam: IXposedHookZygoteInit.StartupParam) {
        if (mHookClassMap.isEmpty()) {
            runCatching {
                val pathClassLoader =
                    PathClassLoader(startupParam.modulePath, ClassLoader.getSystemClassLoader())
                val pathList = XposedHelpers.getObjectField(pathClassLoader, "pathList")
                val dexElements = XposedHelpers.getObjectField(pathList, "dexElements") as Array<*>
                for (element in dexElements) {
                    val dexFile = XposedHelpers.getObjectField(element, "dexFile") as DexFile
                    val enumeration = dexFile.entries()
                    while (enumeration.hasMoreElements()) {
                        val className = enumeration.nextElement()
                        if (!className.contains("$") && className.contains(HOOK_MODULE_PACKAGE)) {
                            runCatching {
                                val cls = Class.forName(className)
                                val annotation = cls.getAnnotation(HookModule::class.java)
                                annotation?.let {
                                    mHookClassMap[it.packageName] = cls
                                }
                            }
                        }
                    }
                }
            }.onFailure {
                if (BuildConfig.DEBUG) Log.e(TAG, "Failed to load hook classes", it)
            }
            if (BuildConfig.DEBUG) Log.d(TAG, "mHookClassMap = $mHookClassMap")
        }
    }
}
