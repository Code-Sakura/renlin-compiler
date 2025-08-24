import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    application
    signing
    id("com.vanniktech.maven.publish")
}

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
}




publishing {
    // Configure all publications
    publications.withType<MavenPublication> {
        pom {
            artifactId = "net.kigawa.renlin-compiler.gradle.plugin"
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
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    if (project.hasProperty("mavenCentralUsername") ||
        System.getenv("ORG_GRADLE_PROJECT_mavenCentralUsername") != null
    )
        signAllPublications()
}