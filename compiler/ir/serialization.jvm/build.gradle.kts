plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    implementation(project(":core:descriptors"))
    api(project(":compiler:ir.tree"))
    api(project(":compiler:ir.serialization.common"))
    implementation(project(":core:descriptors.jvm"))
    api(project(":core:metadata.jvm"))
    implementation(project(":kotlin-util-klib-metadata"))
    implementation(project(":core:deserialization.common.jvm"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:frontend.java"))
}

optInToUnsafeDuringIrConstructionAPI()

sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}
