plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.9.0")

    compileOnly("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
}

kotlin {
    jvmToolchain(8)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["kotlin"])
            groupId = "net.kigawa"
            artifactId = "kotlin-plugin"
            version = "1.0.0"
        }
    }
    repositories {
        mavenLocal()
    }
}