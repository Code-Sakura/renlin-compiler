package com.example

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

@Suppress("unused")
@OptIn(ExperimentalCompilerApi::class)
class MyCompilerRegistrar : CompilerPluginRegistrar() {
    // For now, not intended to support K2.
    override val supportsK2: Boolean get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        if (configuration[MyCommandLineProcessor.KEY_ENABLED] == false) {
            return
        }

        val annotations = configuration[MyCommandLineProcessor.KEY_ANNOTATIONS]
            ?: error("MyPlugin is enabled but no annotations are specified.")
        // MyIrGenerationExtension は次のステップで実装
        IrGenerationExtension.registerExtension(MyIrGenerationExtension(annotations))
    }
}
