import plugins.KotlinBuildPublishingPlugin.Companion.DEFAULT_MAIN_PUBLICATION_NAME
import plugins.signLibraryPublication

description = "kotlin-gradle-statistics"

plugins {
    `java-library`
    id("org.jetbrains.kotlin.jvm")
    //    id("jps-compatible")
    `maven-publish`
}

configureKotlinCompileTasksGradleCompatibility()
configureCommonPublicationSettingsForGradle(signLibraryPublication)
extensions.extraProperties["kotlin.stdlib.default.dependency"] = "false"

dependencies {
//    libraries/stdlib/build/libs/kotlin-stdlib-2.4.255-SNAPSHOT.jar!/META-INF/kotlin-stdlib-jdk7.kotlin_module Module was compiled with an incompatible version of Kotlin.
//    The binary version of its metadata is 2.4.0, expected version is 2.2.0.
//    compileOnly(kotlinStdlib())
//    2.2.21
    val coreDepsVersion = libs.versions.kotlin.`for`.gradle.plugins.compilation.get()
    println("kotlin-gradle-build-metrics:coreDepsVersion=${coreDepsVersion}")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$coreDepsVersion")

    testImplementation(kotlinTest("junit"))
    testImplementation(libs.junit4)
}

//projectTest {
//    workingDir = rootDir
//}

publishing {
    publications {
        register<MavenPublication>(DEFAULT_MAIN_PUBLICATION_NAME) {
            from(components["java"])
        }
    }
}
sourcesJar()
javadocJar()
