plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

description = "Common klib reader and writer"

dependencies {
//    api(kotlinStdlib())
    val coreDepsVersion = libs.versions.kotlin.`for`.gradle.plugins.compilation.get()
    println("kotlin-util-klib:coreDepsVersion=${coreDepsVersion}")
    api("org.jetbrains.kotlin:kotlin-stdlib:$coreDepsVersion")
    api(project(":kotlin-util-io"))

    compileOnly(project(":core:metadata")) { exclude("org.jetbrains.kotlin", "kotlin-stdlib") }

    embedded(project(":core:metadata")) { isTransitive = false }

//    testImplementation(libs.junit4)
}

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}

configureKotlinCompileTasksGradleCompatibility()

publish()

standardPublicJars()