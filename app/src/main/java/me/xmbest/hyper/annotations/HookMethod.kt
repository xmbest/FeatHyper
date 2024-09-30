package me.xmbest.hyper.annotations


/**
 * 自定义需要Hook的方法注解
 * @param value 开关
 * @param defaultEnable 调试使用
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class HookMethod(
    val value: String = "",
    val defaultEnable: Boolean = false
)
