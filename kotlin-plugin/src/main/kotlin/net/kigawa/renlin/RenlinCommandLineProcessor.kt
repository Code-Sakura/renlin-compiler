package net.kigawa.renlin

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@OptIn(ExperimentalCompilerApi::class)
class RenlinCommandLineProcessor : CommandLineProcessor {
    companion object {
        val KEY_ENABLED = CompilerConfigurationKey.create<Boolean>("renlin-compiler-enabled")
        val KEY_ANNOTATIONS = CompilerConfigurationKey.create<List<String>>("renlin-compiler-annotations")
    }

    override val pluginId: String = "renlin-compiler"
    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption(
            optionName = "enabled",
            valueDescription = "true|false",
            description = "Whether RenlinPlugin is enabled or not.",
            required = false,
        ),
        CliOption(
            optionName = "renlinCompilerAnnotation",
            valueDescription = "annotation",
            description = "Annotation to be processed by RenlinPlugin.",
            allowMultipleOccurrences = true,
            required = false,
        ),
    )

    override fun processOption(
        option: AbstractCliOption,
        value: String,
        configuration: CompilerConfiguration,
    ) = when (option.optionName) {
        "enabled" -> configuration.put(KEY_ENABLED, value.toBoolean())
        "renlinCompilerAnnotation" -> configuration.appendList(KEY_ANNOTATIONS, value)
        else -> error("Unexpected config option ${option.optionName}")
    }
}