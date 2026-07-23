plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    implementation(project(":compiler:ir.psi2ir"))
    api(project(":compiler:fir:fir2ir"))
    api(project(":compiler:ir.serialization.common"))
    implementation(project(":js:js.frontend"))

    implementation(project(":compiler:ir.backend.common"))
    implementation(project(":compiler:fir:fir-serialization"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:serialization"))
    implementation(project(":wasm:wasm.config"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:deserialization"))
    compileOnly(intellijCore())
    compileOnly(project(":compiler:cli-common"))
    implementation(project(":kotlin-util-klib-metadata"))
}

optInToUnsafeDuringIrConstructionAPI()

sourceSets {
    "main" { projectDefault() }
}
