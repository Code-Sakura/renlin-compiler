
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}



plugins {
    kotlin("jvm") version "2.0.21"
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
    implementation("net.kigawa:kotlin-plugin:1.0.0")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}


tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("net.kigawa.sample.MainKt")
}