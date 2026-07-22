plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    api(project(":compiler:ir.tree"))
    implementation(project(":compiler:serialization"))
    implementation(project(":compiler:resolution"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:deserialization"))
    implementation(project(":core:deserialization.common"))
    api(project(":kotlin-util-klib"))
    implementation(project(":kotlin-util-klib-metadata"))
    api(project(":compiler:util"))
    implementation(project(":compiler:psi"))
    implementation(project(":compiler:frontend.common-psi"))
    compileOnly(commonDependency("org.jetbrains.kotlin:kotlin-reflect")) { isTransitive = false }
    compileOnly(libs.intellij.fastutil)

    compileOnly(intellijCore())
    compileOnly(project(":compiler:cli-common"))
}

optInToUnsafeDuringIrConstructionAPI()
optInToObsoleteDescriptorBasedAPI()

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
