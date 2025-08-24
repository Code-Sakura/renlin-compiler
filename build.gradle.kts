plugins {
    kotlin("multiplatform") version "2.0.21" apply false
    kotlin("jvm") version "2.0.21" apply false
}

// Root project configuration
val projectVersion = "1.3.0"

allprojects {
    group = "net.kigawa"
    version = projectVersion
}

// This is a root project that aggregates submodules
// Individual modules (gradle-plugin, kotlin-plugin) handle their own publishing