
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}



plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("net.kigawa.renlin-compiler") version "1.0.0"
}


group = "net.kigawa"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("net.kigawa:renlin-compiler-kotlin-plugin:1.0.0")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
}


tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("net.kigawa.sample.MainKt")
}