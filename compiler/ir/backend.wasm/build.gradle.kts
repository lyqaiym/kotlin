plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    api(project(":compiler:cli-base"))
    api(project(":compiler:util"))
//    api(project(":compiler:frontend"))
    implementation(project(":compiler:frontend"))
//    api(project(":compiler:backend-common"))
    api(project(":compiler:ir.tree"))
    api(project(":compiler:ir.backend.common"))
    api(project(":compiler:ir.inline"))
    api(project(":compiler:ir.serialization.common"))
    api(project(":compiler:ir.serialization.js"))
    api(project(":js:js.ast"))
    api(project(":compiler:backend.js"))
    api(project(":wasm:wasm.ir"))

    implementation(project(":wasm:wasm.frontend"))
    implementation(project(":wasm:wasm.config"))
    implementation(project(":core:descriptors"))
    implementation(project(":js:js.frontend"))
    implementation(project(":js:js.serializer"))
    implementation(project(":compiler:ir.psi2ir"))

    compileOnly(intellijCore())
}

optInToUnsafeDuringIrConstructionAPI()

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
