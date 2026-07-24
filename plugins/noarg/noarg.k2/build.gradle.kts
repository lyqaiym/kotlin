description = "Kotlin NoArg Compiler Plugin (K2)"

plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    compileOnly(project(":compiler:fir:cones"))
    compileOnly(project(":compiler:fir:tree"))
    compileOnly(project(":compiler:fir:resolve"))
    compileOnly(project(":compiler:fir:checkers"))
    compileOnly(project(":compiler:ir.backend.common"))
    compileOnly(project(":compiler:fir:entrypoint"))
    compileOnly(project(":compiler:frontend.common-psi"))
    compileOnly(intellijCore())
    runtimeOnly(kotlinStdlib())
}

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}

runtimeJar()
sourcesJar()
javadocJar()
