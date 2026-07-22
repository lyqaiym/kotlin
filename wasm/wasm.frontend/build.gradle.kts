plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    api(project(":compiler:util"))
    implementation(project(":compiler:container"))
    implementation(project(":core:descriptors"))
    implementation(project(":compiler:frontend"))
    implementation(project(":js:js.frontend"))
    implementation(project(":compiler:resolution"))
    api(project(":compiler:resolution.common"))
    api(project(":wasm:wasm.config"))
    compileOnly(intellijCore())
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
