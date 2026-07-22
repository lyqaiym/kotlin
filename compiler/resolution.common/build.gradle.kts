plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    api(project(":core:compiler.common"))
    api(project(":compiler:util"))
    api(project(":core:language.version-settings"))
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
