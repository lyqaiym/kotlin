plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

publish()
sourcesJar()
javadocJar()
configureKotlinCompileTasksGradleCompatibility()

dependencies {
//    compileOnly(kotlinStdlib())
    val coreDepsVersion = libs.versions.kotlin.`for`.gradle.plugins.compilation.get()
    println("kotlin-gradle-build-metrics:coreDepsVersion=${coreDepsVersion}")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$coreDepsVersion")
    testImplementation(kotlinTest("junit"))
}

tasks {
    apiBuild {
        inputJar.value(jar.flatMap { it.archiveFile })
    }
}