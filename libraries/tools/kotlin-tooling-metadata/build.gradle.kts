plugins {
    java
    kotlin("jvm")
    //    id("jps-compatible")
}

publish()
sourcesJar()
javadocJar()
configureKotlinCompileTasksGradleCompatibility()

dependencies {
//    libraries/stdlib/build/libs/kotlin-stdlib-2.4.255-SNAPSHOT.jar!/META-INF/kotlin-stdlib-jdk7.kotlin_module Module was compiled with an incompatible version of Kotlin.
//    The binary version of its metadata is 2.4.0, expected version is 2.2.0.
//    compileOnly(kotlinStdlib())
//    2.2.21
    val coreDepsVersion = libs.versions.kotlin.`for`.gradle.plugins.compilation.get()
    println("kotlin-gradle-build-metrics:coreDepsVersion=${coreDepsVersion}")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$coreDepsVersion")
    implementation(commonDependency("com.google.code.gson:gson"))
    testImplementation(kotlinTest("junit"))
}
