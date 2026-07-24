plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    api(project(":core:metadata.jvm"))
    api(project(":core:deserialization.common"))
    api(project(":core:deserialization.common.jvm"))
    implementation(project(":core:deserialization"))
    implementation(project(":core:compiler.common.jvm"))
    compileOnly(intellijCore())
    compileOnly(libs.kotlinx.coroutines.core.jvm)
    compileOnly(libs.intellij.asm)

    implementation(project(":core:descriptors"))
    implementation(project(":core:descriptors.jvm"))
    api(project(":compiler:psi"))

}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
