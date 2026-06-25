import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jetbrains.kotlin.konan.target.Family

buildscript {
    dependencies {
//        classpath(libs.gson)
    }
}

plugins {
    kotlin("jvm")
}

val isNativeBuildToolsProject = rootProject.name == "native-build-tools"
val isPerformanceProject = rootProject.name == "performance"

if (!isNativeBuildToolsProject) {
    // The module is shared between the main project and 'native-build-tools',
    // in which there is no 'jps-compatible' plugin configured.
    apply(plugin = "jps-compatible")
}

repositories {
//    mavenCentral()
}

dependencies {
//    implementation(libs.gson)
//    implementation(libs.kotlinx.coroutines.core)
    println("dependencies:rootProject.name=${rootProject.name}")
    println("dependencies:isNativeBuildToolsProject=${isNativeBuildToolsProject}")
    println("dependencies:isPerformanceProject=${isPerformanceProject}")
    // KT-61897: Workaround for https://github.com/gradle/gradle/issues/26358
    // (wrong conflict resolution, causing selection of not the latest version of `:kotlin-util-klib` module)
    if (isNativeBuildToolsProject || isPerformanceProject) {
        println("dependencies:kotlin-native-utils1")
//        implementation("org.jetbrains.kotlin:kotlin-native-utils:${project.bootstrapKotlinVersion}")
    } else {
        println("dependencies:kotlin-native-utils2")
//        implementation(project(":native:kotlin-native-utils"))
    }
    implementation(project(":native:kotlin-native-utils"))
}

val family by tasks.registering(Sync::class) {
    val ohos = Family.values().joinToString(separator = ",")
    println("family6=${ohos}")
}

group = "org.jetbrains.kotlin"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        optIn.addAll(
            listOf(
                "kotlin.ExperimentalStdlibApi",
                "kotlin.RequiresOptIn",
            )
        )
        freeCompilerArgs.addAll(
            listOf(
                "-Xskip-prerelease-check",
                "-Xsuppress-version-warnings",
            )
        )
    }
}
