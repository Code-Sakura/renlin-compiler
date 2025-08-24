plugins {
    kotlin("multiplatform")
    `maven-publish`
    signing
}

group = "net.kigawa"
version = "1.0.0"

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
                implementation(kotlin("test-junit5"))
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

// Version from environment or project property
version = project.findProperty("version") as String? ?: "1.0.0"

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("Renlin AutoFill Annotation")
            description.set("Multiplatform annotation library for automatic parameter value injection, supporting both JVM and JavaScript")
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