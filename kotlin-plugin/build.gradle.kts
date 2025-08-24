plugins {
    kotlin("multiplatform")
}

group = "net.kigawa"
version = "1.3.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

kotlin {
    jvmToolchain(17)

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }

    js(IR) {
        browser()
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.0.21")
                compileOnly("com.google.auto.service:auto-service:1.1.1")
            }
        }

        val jvmTest by getting {
            dependencies {
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}


