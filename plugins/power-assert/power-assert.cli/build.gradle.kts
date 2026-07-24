description = "Kotlin Power-Assert Compiler Plugin (CLI)"

plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    compileOnly(project(":compiler:cli"))

    implementation(project(":kotlin-power-assert-compiler-plugin.backend"))
}

optInToExperimentalCompilerApi()

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}

runtimeJar()
sourcesJar()
javadocJar()
