package net.kigawa.renlin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@Suppress("unused")
class RenlinCompilerPlugin : KotlinCompilerPluginSupportPlugin {
    // プラグイン適用時の処理
    // extension（プラグインの設定項目）をgradleに追加する
    override fun apply(target: Project) {
        target.extensions.create(
            "renlinCompiler",
            RenlinCompilerExtension::class.java,
        )
    }

    // プラグインが適用可能かどうか
    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return kotlinCompilation.project.plugins.hasPlugin(RenlinCompilerPlugin::class.java)
    }

    // プラグインをコンパイルに適用する処理
    // extensionで設定された項目をコンパイラに伝える
    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        val extension = kotlinCompilation.project.extensions.findByType(RenlinCompilerExtension::class.java) ?: RenlinCompilerExtension()

        val annotationOptions = extension.annotations.map {
            SubpluginOption(key = "renlinCompilerAnnotation", value = it)
        }
        val enabledOption = SubpluginOption(
            key = "enabled",
            value = extension.enabled.toString(),
        )
        return kotlinCompilation.target.project.provider {
            listOf(enabledOption) + annotationOptions
        }
    }

    // プラグインのIDを返す
    override fun getCompilerPluginId(): String {
        return "renlin-compiler"
    }

    // Kotlinコンパイラプラグインの実装部アーティファクトを返す
    override fun getPluginArtifact(): SubpluginArtifact {
        return SubpluginArtifact(
            groupId = "net.kigawa.renlin-compiler",
            artifactId = "renlin-kotlin-plugin-jvm",
            version = "1.3.0",
        )
    }
}