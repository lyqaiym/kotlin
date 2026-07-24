description = "Kotlin Serialization Compiler Plugin (Backend)"

plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    compileOnly(project(":compiler:backend"))
    compileOnly(project(":compiler:ir.backend.common"))
    compileOnly(project(":compiler:backend.jvm"))
    compileOnly(project(":compiler:backend.jvm.codegen"))
    compileOnly(project(":compiler:backend.jvm.lower"))
    compileOnly(project(":compiler:ir.tree"))
    compileOnly(project(":compiler:fir:fir2ir"))
    compileOnly(project(":compiler:fir:tree"))
    compileOnly(project(":compiler:fir:fir-deserialization"))
    compileOnly(project(":kotlin-util-klib-metadata"))
    compileOnly(project(":compiler:cli-common"))

    implementation(project(":compiler:backend.common.jvm"))
    implementation(project(":compiler:frontend"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:deserialization"))
    implementation(project(":kotlinx-serialization-compiler-plugin.common"))
    implementation(project(":kotlinx-serialization-compiler-plugin.k1"))

    compileOnly(intellijCore())
}

optInToUnsafeDuringIrConstructionAPI()

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}

optInToK1Deprecation()

runtimeJar()
sourcesJar()
javadocJar()
