import org.jetbrains.kotlin.konan.target.Family

plugins {
    kotlin("jvm")
}

val commonCompilerModules: Array<String> by rootProject.extra

val excludedCompilerModules = listOf(
    ":compiler:cli",
    ":compiler:javac-wrapper",
    ":compiler:incremental-compilation-impl"
)

val projects = commonCompilerModules.asList() - excludedCompilerModules + listOf(
    ":kotlin-compiler-runner-unshaded",
    ":kotlin-preloader",
    ":daemon-common",
    ":kotlin-daemon-client"
)

publishJarsForIde(
    projects = projects,
    libraryDependencies = listOf(protobufFull())
)

val family by tasks.registering(Sync::class) {
    val ohos = Family.values().joinToString(separator = ",")
    println("family8=${ohos}")
}