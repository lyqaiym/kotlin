plugins {
//    kotlin("js").apply(false)
    kotlin("multiplatform")
}

group = "com.example"
version = "1.0"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}