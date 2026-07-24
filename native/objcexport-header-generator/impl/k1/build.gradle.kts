plugins {
    kotlin("jvm")
}

sourceSets {
    "main" { projectDefault() }
//    "test" { projectDefault() }
}

dependencies {
    api(project(":native:objcexport-header-generator"))
    implementation(project(":compiler:cli-base"))
    implementation(project(":compiler:ir.objcinterop"))
    implementation(project(":compiler:ir.serialization.native"))
    implementation(project(":core:descriptors"))
    implementation(project(":native:frontend.native"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:resolution"))
    implementation(project(":kotlin-util-klib-metadata"))
//    testImplementation(projectTests(":native:objcexport-header-generator"))
}

kotlin {
    compilerOptions {
        optIn.add("org.jetbrains.kotlin.backend.konan.InternalKotlinNativeApi")
    }
}

//testsJar()

//objCExportHeaderGeneratorTest("test")
