plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `maven-publish`
    signing
}

group = "net.kigawa"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("renlinCompiler") {
            id = "net.kigawa.renlin-compiler"
            implementationClass = "net.kigawa.renlin.RenlinCompilerPlugin"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib", "1.9.0"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:1.9.0")

    compileOnly("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
}

kotlin {
    jvmToolchain(8)
}

java {
    withSourcesJar()
    withJavadocJar()
}

// Version from environment or project property
version = project.findProperty("version") as String? ?: "1.0.0"

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["kotlin"])
            groupId = "net.kigawa"
            artifactId = "renlin-compiler-gradle-plugin"
            
            pom {
                name.set("Renlin Compiler Gradle Plugin")
                description.set("Gradle plugin that enables automatic value injection for @AutoFill annotated parameters using Kotlin Compiler Plugin")
                url.set("https://github.com/kigawa01/kcp-for-renlin")
                
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
                    connection.set("scm:git:git://github.com/kigawa01/kcp-for-renlin.git")
                    developerConnection.set("scm:git:ssh://github.com:kigawa01/kcp-for-renlin.git")
                    url.set("https://github.com/kigawa01/kcp-for-renlin")
                }
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