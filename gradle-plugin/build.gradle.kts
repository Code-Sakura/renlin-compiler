plugins {
    kotlin("jvm")
    `java-gradle-plugin`
}

group = "net.kigawa"
version = "1.3.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    website.set("https://github.com/Code-Sakura/renlin-compiler")
    vcsUrl.set("https://github.com/Code-Sakura/renlin-compiler.git")

    plugins {
        create("renlinCompiler") {
            id = "net.kigawa.renlin-compiler"
            implementationClass = "net.kigawa.renlin.RenlinCompilerPlugin"
            displayName = "Renlin Compiler Plugin"
            description = "Kotlin Compiler Plugin for automatic value injection with @AutoFill annotation"
            tags.set(listOf("kotlin", "compiler-plugin", "annotation"))
        }
    }
}

dependencies {
    implementation(kotlin("stdlib", "2.0.21"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.0.21")

    compileOnly("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
}

kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
    withJavadocJar()
}


