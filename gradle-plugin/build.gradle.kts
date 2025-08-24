plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `maven-publish`
    signing
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


publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Renlin Compiler Gradle Plugin")
            description.set("Gradle plugin that enables automatic value injection for @AutoFill annotated parameters using Kotlin Compiler Plugin")
            url.set("https://github.com/Code-Sakura/renlin-compiler")
            
            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            
            developers {
                developer {
                    id.set("kigawa01")
                    name.set("kigawa")
                    email.set("kigawa.inbox@gmail.com")
                }
            }
            
            scm {
                connection.set("scm:git:https://github.com/Code-Sakura/renlin-compiler.git")
                developerConnection.set("scm:git:https://github.com/Code-Sakura/renlin-compiler.git")
                url.set("https://github.com/Code-Sakura/renlin-compiler")
            }
        }
    }
    repositories {
        mavenLocal()
    }
}


signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    val hasSigningCredentials = (System.getenv("GPG_SIGNING_KEY") != null && System.getenv("GPG_SIGNING_PASSWORD") != null) ||
        (signingKey != null && signingPassword != null)
    
    if (hasSigningCredentials) {
        useInMemoryPgpKeys(
            System.getenv("GPG_SIGNING_KEY") ?: signingKey,
            System.getenv("GPG_SIGNING_PASSWORD") ?: signingPassword
        )
        sign(publishing.publications)
    }
}