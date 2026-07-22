plugins {
    kotlin("jvm")
//    id("jps-compatible")
}

sourceSets {
    "main" { projectDefault() }
    "test" { projectDefault() }
}

//projectTest {
//    workingDir = rootDir
//}

dependencies {
    implementation(project(":core:deserialization"))
    api(project(":compiler:psi"))
    implementation(project(":core:descriptors"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:frontend.java"))
    api(project(":analysis:decompiled:decompiler-to-file-stubs"))
    api(project(":analysis:decompiled:decompiler-to-psi"))
    api(project(":analysis:decompiled:decompiler-to-stubs"))
    implementation(project(":kotlin-util-klib-metadata"))

    implementation(project(":js:js.serializer"))

    compileOnly(intellijCore())

    testApi(platform(libs.junit.bom))
    testImplementation(libs.junit4)
    testCompileOnly(libs.junit.jupiter.api) // the annotations are misused and have no effect
    testImplementation(projectTests(":compiler:tests-common"))
    testImplementation(projectTests(":compiler:tests-common-new"))
    testImplementation(projectTests(":analysis:decompiled:decompiler-to-file-stubs"))
}

testsJar()
