rootProject.name = "native-build-tools"

pluginManagement {
    includeBuild("../../dependencies/kotlin-build-gradle-plugin")
//    apply(from = "../../repo/scripts/cache-redirector.settings.gradle.kts")
//    apply(from = "../../repo/scripts/kotlin-bootstrap.settings.gradle.kts")
    includeBuild("../../repo/gradle-settings-conventions")

//    repositories {
//        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-dependencies")
//        mavenCentral()
//        gradlePluginPortal()
//    }
    repositories {
        maven("https://redirector.kotlinlang.org/maven/kotlin-dependencies")
        mavenCentral { setUrl("https://cache-redirector.jetbrains.com/maven-central") }
        gradlePluginPortal()
    }
}

plugins {
    id("kotlin-build-helpers")
//    Plugin [id: 'org.jetbrains.kotlin.jvm', apply: false] was not found in any of the following sources:
    id("kotlin-bootstrap")
    id("jvm-toolchain-provisioning")
    id("develocity")
    id("kotlin-daemon-config")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

includeBuild("../../repo/gradle-build-conventions")

buildscript {
    val buildGradlePluginVersion = extra["kotlin.build.gradlePlugin.version"]
//    dependencies {
//        classpath("org.jetbrains.kotlin:kotlin-build-gradle-plugin:$buildGradlePluginVersion")
//    }
}