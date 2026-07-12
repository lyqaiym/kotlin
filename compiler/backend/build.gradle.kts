plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    api(project(":kotlin-annotations-jvm"))
    api(project(":compiler:util"))
    implementation(project(":compiler:backend-common"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:frontend.java"))
    implementation(project(":compiler:serialization"))
    implementation(project(":compiler:backend.common.jvm"))
    compileOnly(intellijCore())
    compileOnly(libs.intellij.fastutil)
    compileOnly(libs.intellij.asm)
    compileOnly(libs.guava)
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}
