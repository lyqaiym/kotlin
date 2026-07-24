plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    api(project(":compiler:util"))
    implementation(project(":core:descriptors"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:resolution"))
//    api(project(":compiler:backend-common"))
    api(project(":compiler:ir.tree"))
    api(project(":compiler:ir.interpreter"))
    implementation(project(":kotlin-util-klib"))
    compileOnly(intellijCore())
    implementation(project(":compiler:backend-common"))
    testImplementation(kotlinTest("junit"))
    testImplementation(projectTests(":compiler:tests-common-new"))
}

optInToUnsafeDuringIrConstructionAPI()

sourceSets {
    "main" { projectDefault() }
    "test" { projectDefault() }
}

