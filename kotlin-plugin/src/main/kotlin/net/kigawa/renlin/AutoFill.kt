package net.kigawa.renlin

/**
 * Marks a parameter for automatic value injection when passed as null.
 * When applied to a function parameter, the plugin will automatically generate
 * and inject a value if null is passed for this parameter.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoFill