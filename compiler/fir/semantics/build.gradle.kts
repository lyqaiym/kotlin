plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    api(project(":compiler:fir:providers"))
    implementation(project(":core:util.runtime"))

    compileOnly(libs.guava)
}

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}
