package me.xmbest.hyper.annotations


/**
 * 自定义需要Hook的类注解
 * @param packageName 包名
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class HookModule(val packageName: String = "")
