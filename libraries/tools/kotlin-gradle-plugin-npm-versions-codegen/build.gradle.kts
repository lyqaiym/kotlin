plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlinStdlib("jdk8"))
    implementation(libs.ktor.client.cio)
    implementation(libs.gson)
    implementation("org.apache.velocity:velocity-engine-core:2.3")
    implementation(libs.kotlinx.serialization.core)
}

val generateNpmVersions by generator(
    "org.jetbrains.kotlin.generators.gradle.targets.js.MainKt",
    sourceSets["main"]
) {
    systemProperty(
        "org.jetbrains.kotlin.generators.gradle.targets.js.outputSourceRoot",
        project(":kotlin-gradle-plugin").projectDir.resolve("src/common/kotlin").absolutePath
    )
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.apache.commons" && requested.name == "commons-lang3") {
            useVersion(libs.versions.commons.lang.get())
            because("CVE-2025-48924")
        }
    }
}