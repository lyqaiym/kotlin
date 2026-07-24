plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    api(project(":compiler:ir.tree"))
    api(project(":compiler:ir.inline"))
    implementation(project(":compiler:backend"))
    implementation(project(":compiler:backend.jvm"))
    implementation(project(":compiler:backend-common"))
    implementation(project(":compiler:backend.common.jvm"))
    implementation(project(":compiler:ir.backend.common"))
    implementation(project(":compiler:frontend"))
    implementation(project(":compiler:frontend.common.jvm"))
    implementation(project(":compiler:resolution"))
    implementation(project(":core:descriptors"))
    implementation(project(":core:descriptors.jvm"))
    implementation(project(":compiler:frontend.java"))
    compileOnly(intellijCore())
}

optInToUnsafeDuringIrConstructionAPI()
kotlin {
    compilerOptions.optIn.add("org.jetbrains.kotlin.ir.util.JvmIrInlineExperimental")
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}
