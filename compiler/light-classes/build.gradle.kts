plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    api(project(":analysis:light-classes-base"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:descriptors.jvm"))
    api(project(":compiler:util"))
    api(project(":compiler:backend"))
    implementation(project(":compiler:backend-common"))
    implementation(project(":compiler:backend.common.jvm"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:frontend.java"))
    implementation(project(":compiler:resolution"))
    compileOnly(intellijCore())
    compileOnly(libs.intellij.asm)
    compileOnly(libs.guava)
    compileOnly(libs.intellij.fastutil)
}

sourceSets {
    "main" { projectDefault() }
}
