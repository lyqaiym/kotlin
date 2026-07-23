plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    implementation(project(":core:descriptors"))
    implementation(project(":core:deserialization"))
    implementation(project(":core:metadata.jvm"))
    api(project(":compiler:backend"))
    api(project(":compiler:ir.tree"))
    api(project(":compiler:ir.backend.common"))
    implementation(project(":compiler:backend.common.jvm"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:resolution"))
    implementation(project(":compiler:serialization"))
    implementation(project(":compiler:frontend.java"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:descriptors.jvm"))
    compileOnly(intellijCore())
    compileOnly(libs.intellij.asm)
}

optInToUnsafeDuringIrConstructionAPI()

sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}
