package net.kigawa.renlin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

@Suppress("unused")
@OptIn(ExperimentalCompilerApi::class)
class RenlinCompilerRegistrar : CompilerPluginRegistrar() {
    // For now, not intended to support K2.
    override val supportsK2: Boolean get() = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val enabled = configuration[RenlinCommandLineProcessor.KEY_ENABLED] ?: true
        if (!enabled) {
            return
        }

        val annotations = configuration[RenlinCommandLineProcessor.KEY_ANNOTATIONS] ?: emptyList()
        IrGenerationExtension.registerExtension(RenlinIrGenerationExtension(annotations))
    }
}