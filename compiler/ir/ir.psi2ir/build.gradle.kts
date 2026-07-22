plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    api(project(":compiler:util"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:resolution"))
    implementation(project(":compiler:resolution.common"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:deserialization"))
    api(project(":compiler:backend-common"))
    api(project(":compiler:ir.tree"))
    compileOnly(intellijCore())
}

optInToUnsafeDuringIrConstructionAPI()
optInToObsoleteDescriptorBasedAPI()

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
