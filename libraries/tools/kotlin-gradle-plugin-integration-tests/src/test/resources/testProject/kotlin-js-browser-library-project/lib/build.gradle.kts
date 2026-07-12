plugins {
//    kotlin("js")
    kotlin("multiplatform")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation(project(":base"))
}

kotlin {
    js {
        useCommonJs()
        browser {
        }
    }
}