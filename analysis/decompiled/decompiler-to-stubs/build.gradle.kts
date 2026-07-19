plugins {
    kotlin("jvm")
//    id("jps-compatible")
}

dependencies {
    api(project(":compiler:psi"))
    api(project(":core:deserialization.common"))
    api(project(":core:deserialization.common.jvm"))
    implementation(project(":core:deserialization"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:compiler.common.jvm"))
    implementation(project(":kotlin-util-klib"))
    testImplementation(projectTests(":compiler:tests-common-new"))

    api(intellijCore())
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}


