pluginManagement {
    includeBuild("../../dependencies/kotlin-build-gradle-plugin")
//    apply(from = "../scripts/cache-redirector.settings.gradle.kts")
//    apply(from = "../scripts/kotlin-bootstrap.settings.gradle.kts")
//    Plugin [id: 'org.jetbrains.kotlin.jvm', apply: false] was not found in any of the following sources:
    apply(from = "cache-redirector/src/main/kotlin/cache-redirector.settings.gradle.kts")
    apply(from = "kotlin-bootstrap/src/main/kotlin/kotlin-bootstrap.settings.gradle.kts")

    repositories {
        maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-dependencies")
        mavenCentral()
        gradlePluginPortal()
    }
}

buildscript {
    val buildGradlePluginVersion = extra.get("kotlin.build.gradlePlugin.version")
    dependencies {
//        One artifact failed verification: kotlin-build-gradle-plugin-0.0.40.jar
//        classpath("org.jetbrains.kotlin:kotlin-build-gradle-plugin:$buildGradlePluginVersion")
    }
}

plugins {
    // Versions here should be also synced with the versions in 'libs.versions.toml'
//    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
//    id("com.gradle.develocity") version("3.17.5")
    id("kotlin-build-helpers")
    // Versions here should be also synced with the versions in 'libs.versions.toml'
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("com.gradle.develocity") version ("4.2.2")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

include(":develocity")
include(":jvm-toolchain-provisioning")
include(":kotlin-daemon-config")
include(":kotlin-daemon-config")
include(":internal-gradle-setup")
include(":cache-redirector")
include(":kotlin-bootstrap")

// Sync below to the content of develocity settings plugin
val buildProperties = getKotlinBuildPropertiesForSettings(settings)

develocity {
    val isTeamCity = buildProperties.isTeamcityBuild
    if (!buildProperties.buildScanServer.isNullOrEmpty()) {
        server.set(buildProperties.buildScanServer)
    }
    buildScan {
        capture {
            uploadInBackground = !isTeamCity
        }

        val overriddenUsername = (buildProperties.getOrNull("kotlin.build.scan.username") as? String)?.trim()
        val overriddenHostname = (buildProperties.getOrNull("kotlin.build.scan.hostname") as? String)?.trim()
        if (buildProperties.isJpsBuildEnabled) {
            tag("JPS")
        }
        obfuscation {
            ipAddresses { _ -> listOf("0.0.0.0") }
            hostname { _ -> overriddenHostname ?: "concealed" }
            username { originalUsername ->
                when {
                    isTeamCity -> "TeamCity"
                    overriddenUsername.isNullOrEmpty() -> "concealed"
                    overriddenUsername == "<default>" -> originalUsername
                    else -> overriddenUsername
                }
            }
        }
    }
}

buildCache {
    local {
        isEnabled = buildProperties.localBuildCacheEnabled
        if (buildProperties.localBuildCacheDirectory != null) {
            directory = buildProperties.localBuildCacheDirectory
        }
    }
    if (develocity.server.isPresent) {
        remote(develocity.buildCache) {
            isPush = buildProperties.pushToBuildCache
            val remoteBuildCacheUrl = buildProperties.buildCacheUrl?.trim()
            isEnabled = remoteBuildCacheUrl != "" // explicit "" disables it
            if (!remoteBuildCacheUrl.isNullOrEmpty()) {
                server = remoteBuildCacheUrl.removeSuffix("/cache/")
            }
        }
    }
}