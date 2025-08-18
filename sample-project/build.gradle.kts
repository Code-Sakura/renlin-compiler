
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath("net.kigawa:net.kigawa.gradle.plugin:1.0.0")
    }
}

plugins {
    kotlin("jvm") version "1.9.0"
    application
}

apply(plugin = "net.kigawa.renlin-compiler")

group = "net.kigawa"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
}

// プラグイン設定
configure<net.kigawa.renlin.RenlinCompilerExtension> {
    enabled = true
    annotations = listOf("net.kigawa.sample.UseAutoKey")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("net.kigawa.sample.MainKt")
}