plugins {
    kotlin("jvm")
//    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    implementation(project(":compiler:build-tools:kotlin-build-tools-api"))
//    api(project(":compiler:cli-base")) { isTransitive = false }
//    api(project(":compiler:util")) { isTransitive = false }
    api(project(":core:compiler.common")) { isTransitive = false }
    api(project(":compiler:cli-common"))
    api(project(":kotlin-build-common")) { isTransitive = false }
//    api(kotlinStdlib())
    val coreDepsVersion = libs.versions.kotlin.`for`.gradle.plugins.compilation.get()
    api(kotlin("stdlib", coreDepsVersion))
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
