plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    api(project(":compiler:config"))
    implementation(project(":compiler:container"))
    compileOnly(intellijCore())
    compileOnly(libs.guava)
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
