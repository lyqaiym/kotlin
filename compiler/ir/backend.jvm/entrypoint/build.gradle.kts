plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    implementation(project(":compiler:ir.psi2ir"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:frontend.java"))
    implementation(project(":compiler:resolution"))
    implementation(project(":compiler:serialization"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:descriptors.jvm"))
    implementation(project(":core:deserialization"))
    api(project(":compiler:backend.jvm"))
    api(project(":compiler:ir.tree"))
    api(project(":compiler:ir.serialization.jvm"))
    implementation(project(":compiler:backend.jvm.lower"))
    implementation(project(":compiler:backend.jvm.codegen"))
    implementation(project(":kotlin-util-klib"))
    implementation(project(":kotlin-util-klib-metadata"))
    compileOnly(intellijCore())
}

optInToUnsafeDuringIrConstructionAPI()

sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}
