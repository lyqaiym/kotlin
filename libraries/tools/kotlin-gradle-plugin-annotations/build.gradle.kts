plugins {
    kotlin("jvm")
//    id("jps-compatible")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

configureKotlinCompileTasksGradleCompatibility()

publish()
standardPublicJars()

dependencies {
    api(platform(project(":kotlin-gradle-plugins-bom")))

//    libraries/stdlib/build/libs/kotlin-stdlib-2.4.255-SNAPSHOT.jar!/META-INF/kotlin-stdlib-jdk7.kotlin_module Module was compiled with an incompatible version of Kotlin.
//    The binary version of its metadata is 2.4.0, expected version is 2.2.0.
//    compileOnly(kotlinStdlib())
    val coreDepsVersion = libs.versions.kotlin.`for`.gradle.plugins.compilation.get()
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$coreDepsVersion")
}

apiValidation {
    nonPublicMarkers += "org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi"
}

tasks {
    apiBuild {
        inputJar.value(jar.flatMap { it.archiveFile })
    }
}