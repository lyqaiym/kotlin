plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
//    mavenCentral { setUrl("https://cache-redirector.jetbrains.com/maven-central") }
    gradlePluginPortal()
}

//dependencies {
//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api")
//}

kotlin.jvmToolchain(8)
