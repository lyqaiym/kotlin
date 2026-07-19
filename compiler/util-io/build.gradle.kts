plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
//    implementation(kotlinStdlib())
    val coreDepsVersion = libs.versions.kotlin.`for`.gradle.plugins.compilation.get()
    println("kotlin-util-io:coreDepsVersion=${coreDepsVersion}")
    implementation(kotlin("stdlib", coreDepsVersion))
//    testImplementation(libs.junit4)
//    testImplementation(kotlin("test"))
}

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}

configureKotlinCompileTasksGradleCompatibility()

publish()

standardPublicJars()
