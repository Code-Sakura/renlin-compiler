import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("multiplatform") version "2.0.21" apply false
    kotlin("jvm") version "2.0.21" apply false
    application
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    id("com.vanniktech.maven.publish") version "0.29.0"
}

object Conf {
    const val GROUP = "net.kigawa"

    // Base version - will be modified based on branch name if available
    const val BASE_VERSION = "1.3.0"
}

// Determine version based on branch name
// Branch naming convention for releases: release/vx.x.x (e.g., release/v1.0.0)
fun determineVersion(): String {
    // Try to get branch name from different environment variables
    // For pull requests, GITHUB_HEAD_REF contains the source branch name
    // For direct pushes, GITHUB_REF_NAME contains the branch name
    val branchName = System.getenv("GITHUB_HEAD_REF")
        ?: System.getenv("GITHUB_REF_NAME")
        ?: return Conf.BASE_VERSION

    // For main branch, use the base version
    if (branchName == "main") {
        return Conf.BASE_VERSION
    }

    // For release branches in format 'release/vx.x.x', use the version from the branch name
    val releasePattern = Regex("^release/v(\\d+\\.\\d+\\.\\d+)$")
    val matchResult = releasePattern.find(branchName)
    if (matchResult != null) {
        // Extract version number from branch name
        val versionFromBranch = matchResult.groupValues[1]
        return versionFromBranch
    }

    // For other branches, use format: baseVersion-branchName-SNAPSHOT
    // Replace any non-alphanumeric characters with dashes for Maven compatibility
    val sanitizedBranchName = branchName.replace(Regex("[^a-zA-Z0-9]"), "-")
    return "${Conf.BASE_VERSION}-${sanitizedBranchName}-SNAPSHOT"
}

val projectVersion = determineVersion()

group = Conf.GROUP
version = projectVersion
allprojects {
    group = Conf.GROUP
    version = projectVersion
}
repositories {
    mavenCentral()
}

// Root project - no source code, only module aggregation



publishing {
    // Configure all publications
    publications.withType<MavenPublication> {
        // disabled https://github.com/vanniktech/gradle-maven-publish-plugin/issues/754
        // and configured at library build.gradle.kts using `JavadocJar.Dokka("dokkaHtml")`.
        /*
        // Stub javadoc.jar artifact
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })*/

        // Provide artifacts information required by Maven Central
        pom {
            name = "net.kigawa.renlin-compiler.gradle.plugin"
            description = "Kotlin Compiler Plugin for automatic value injection with @AutoFill annotation"
            url = "https://github.com/Code-Sakura/renlin-compiler"
            properties = mapOf(
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