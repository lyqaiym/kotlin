description = "Kotlin Serialization Compiler Plugin (CLI)"

plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    compileOnly(project(":compiler:util"))
    compileOnly(project(":compiler:cli"))
    compileOnly(project(":compiler:plugin-api"))
    compileOnly(project(":compiler:fir:entrypoint"))
    compileOnly(project(":kotlin-util-klib-metadata"))

    implementation(project(":kotlinx-serialization-compiler-plugin.common"))
    implementation(project(":kotlinx-serialization-compiler-plugin.k1"))
    implementation(project(":kotlinx-serialization-compiler-plugin.k2"))
    implementation(project(":kotlinx-serialization-compiler-plugin.backend"))
    implementation(project(":core:descriptors"))
    implementation(project(":compiler:container"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:serialization"))
    implementation(project(":js:js.serializer"))

    compileOnly(intellijCore())
}

optInToExperimentalCompilerApi()

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}

runtimeJar()
sourcesJar()
javadocJar()
