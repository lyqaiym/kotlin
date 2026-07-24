
plugins {
    kotlin("jvm")
    id("gradle-plugin-compiler-dependency-configuration")
    //    id("jps-compatible")
}

dependencies {
    implementation(project(":core:descriptors"))
    implementation(project(":compiler:resolution"))
    api(project(":compiler:util"))
    implementation(project(":compiler:frontend"))
    compileOnly(intellijCore())
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}
