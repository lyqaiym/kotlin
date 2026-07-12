import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

project.updateJvmTarget("1.8")

dependencies {
    api(kotlinStdlib())
    api(project(":kotlin-scripting-common"))
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.core)
}

sourceSets {
    "main" { projectDefault() }
    "test" { projectDefault() }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions.freeCompilerArgs.add("-Xallow-kotlin-package")
}

publish()

runtimeJar()
sourcesJar()
javadocJar()
testsJar()

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.apache.commons" && requested.name == "commons-lang3") {
            useVersion(libs.versions.commons.lang.get())
            because("CVE-2025-48924")
        }
    }
}