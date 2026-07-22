description = "Kotlin Build Common"

plugins {
    kotlin("jvm")
//    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
//    id("java-test-fixtures")
//    id("project-tests-convention")
}

dependencies {
//    implementation(project(":compiler:arguments.common"))
//    implementation(project(":compiler:config.jvm"))
//    implementation(project(":compiler:frontend"))
//    implementation(project(":compiler:frontend.java"))
//    implementation(project(":compiler:resolution"))
    implementation(project(":compiler:serialization"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:descriptors.jvm"))
    implementation(project(":core:deserialization"))
    compileOnly(project(":core:util.runtime"))
    compileOnly(project(":compiler:backend.common.jvm"))
    compileOnly(project(":compiler:util"))
    compileOnly(project(":compiler:cli-common"))
    compileOnly(project(":compiler:frontend.java"))
    compileOnly(project(":js:js.serializer"))
    compileOnly(project(":js:js.config"))
    compileOnly(project(":kotlin-util-klib-metadata"))
    compileOnly(commonDependency("org.jetbrains.kotlin:kotlin-reflect")) { isTransitive = false }

    compileOnly(intellijCore())
    compileOnly(libs.intellij.asm)
    compileOnly(project(":compiler:build-tools:kotlin-build-statistics"))

    testCompileOnly(project(":compiler:cli-common"))
    testApi(projectTests(":compiler:tests-common"))
    testApi(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.junit4)
    testApi(protobufFull())
    testApi(kotlinStdlib())
    testImplementation(project(":compiler:build-tools:kotlin-build-statistics"))
    testImplementation(commonDependency("org.jetbrains.kotlin:kotlin-reflect")) { isTransitive = false }
    testImplementation("org.reflections:reflections:0.10.2")
}

sourceSets {
    "main" { projectDefault() }
    "test" { projectDefault() }
}

testsJar()

//projectTests {
//    testTask(parallel = true, jUnitMode = JUnitMode.JUnit4)
//    testTask("testJUnit5", jUnitMode = JUnitMode.JUnit5, skipInLocalBuild = false)
//}
