plugins {
    kotlin("jvm")
//    id("jps-compatible")
    id("generated-sources")
}

dependencies {
    api(project(":compiler:cli-common"))
    implementation(project(":compiler:resolution"))
    api(project(":compiler:resolution.common"))
    api(project(":compiler:resolution.common.jvm"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:frontend:cfg"))
    implementation(project(":compiler:frontend.java"))
    implementation(project(":compiler:serialization"))
    implementation(project(":compiler:resolution"))
    implementation(project(":compiler:container"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:descriptors.jvm"))
    implementation(project(":core:deserialization"))
    api(project(":compiler:backend.jvm"))
    api(project(":compiler:light-classes"))
    implementation(project(":compiler:javac-wrapper"))
    implementation(project(":kotlin-util-klib-metadata"))

    compileOnly(toolsJarApi())
    compileOnly(intellijCore())
    compileOnly(libs.intellij.fastutil)
    compileOnly(libs.intellij.asm)
    compileOnly(libs.guava)
    runtimeOnly(libs.kotlinx.coroutines.core)
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" { none() }
}

allprojects {
    optInToExperimentalCompilerApi()
}

testsJar {}

//projectTest {
//    workingDir = rootDir
//}

generatedConfigurationKeys("CLIConfigurationKeys")
