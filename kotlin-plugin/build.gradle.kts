import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
plugins {
    kotlin("multiplatform")
    signing
    id("com.vanniktech.maven.publish") version "0.29.0"
}

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




publishing {
    // Configure all publications
    publications.withType<MavenPublication> {
        pom {
            artifactId = "renlin-kotlin-plugin"
            name.set("net.kigawa.renlin-compiler.gradle.plugin")
            description.set("Kotlin Compiler Plugin for automatic value injection with @AutoFill annotation")
            url.set("https://github.com/Code-Sakura/renlin-compiler")
            properties.set(
                mapOf(
                )
            )
            licenses {
                license {
                    name.set("MIT License")
                    url.set("http://www.opensource.org/licenses/mit-license.php")
                }
            }
            developers {
                developer {
                    id.set("net.kigawa")
                    name.set("kigawa")
                    email.set("contact@kigawa.net")
                }
                developer {
                    id.set("io.github.seizavl")
                    name.set("seizavl")
                    email.set("")
                }
            }
            scm {
                connection.set("scm:git:https://github.com/Code-Sakura/renlin-compiler.git")
                developerConnection.set("scm:git:https://github.com/Code-Sakura/renlin-compiler.git")
                url.set("https://github.com/Code-Sakura/renlin-compiler")
            }
        }
    }
}

signing {
    if (project.hasProperty("mavenCentralUsername") ||
        System.getenv("ORG_GRADLE_PROJECT_mavenCentralUsername") != null
    ) {
        useGpgCmd()
        // It is not perfect (fails at some dependency assertions), better handled as
        // `signAllPublications()` (as in vanniktech maven publish plugin) at build.gradle.kts.
        //sign(publishing.publications)
    }
}

mavenPublishing {
    configure(KotlinMultiplatform(javadocJar = JavadocJar.Dokka("dokkaHtml")))
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    if (project.hasProperty("mavenCentralUsername") ||
        System.getenv("ORG_GRADLE_PROJECT_mavenCentralUsername") != null
    )
        signAllPublications()
}