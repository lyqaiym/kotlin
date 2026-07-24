description = "Kotlin SamWithReceiver Compiler Plugin (CLI)"

plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    api(project(":kotlin-sam-with-receiver-compiler-plugin.common"))
    api(project(":kotlin-sam-with-receiver-compiler-plugin.k1"))
    api(project(":kotlin-sam-with-receiver-compiler-plugin.k2"))
    implementation(project(":compiler:container"))
    implementation(project(":compiler:frontend"))
    implementation(project(":core:descriptors"))
    compileOnly(project(":compiler:util"))
    compileOnly(project(":compiler:plugin-api"))
    compileOnly(project(":compiler:fir:entrypoint"))
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
