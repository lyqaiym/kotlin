plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    api(project(":compiler:util"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:serialization"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:deserialization"))
    implementation(project(":core:metadata"))
    api(project(":js:js.ast"))
    api(project(":js:js.config"))
    compileOnly(intellijCore())
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
