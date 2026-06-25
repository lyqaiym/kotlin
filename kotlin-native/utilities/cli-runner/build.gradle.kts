import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jetbrains.kotlin.konan.target.Family

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":compiler:cli-common"))
    implementation(project(":compiler:util"))
    implementation(project(":kotlin-native:Interop:StubGenerator"))
    implementation(project(":kotlin-native:backend.native")) // used by generatePlatformLibraries command for cache generation
    implementation(project(":kotlin-native:common:env"))
    implementation(project(":kotlin-native:common:files"))
    implementation(project(":kotlin-native:endorsedLibraries:kotlinx.cli", configuration = "jvmRuntimeElements"))
    implementation(project(":kotlin-native:klib"))
    implementation(project(":native:cli-native"))
    implementation(project(":native:kotlin-native-utils"))
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions.optIn.addAll(
            listOf(
                    "kotlinx.cinterop.ExperimentalForeignApi",
            )
    )
}

val family by tasks.registering(Sync::class) {
    val ohos = Family.values().joinToString(separator = ",")
    println("family4=${ohos}")
}

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}