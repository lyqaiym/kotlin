plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    compileOnly(project(":compiler:ir.tree"))
    compileOnly(project(":compiler:ir.backend.common"))
    compileOnly(project(":compiler:ir.backend.native"))
    compileOnly(project(":compiler:ir.serialization.common"))
    implementation(project(":core:descriptors"))
    implementation(project(":compiler:frontend.common-psi"))
    compileOnly(intellijCore())
}

optInToUnsafeDuringIrConstructionAPI()

sourceSets {
    "main" { projectDefault() }
    "test" {}
}

