plugins {
    idea
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    api(project(":js:js.ast"))
    compileOnly(intellijCore())
    compileOnly(libs.guava)
    val coreDepsVersion = libs.versions.kotlin.`for`.gradle.plugins.compilation.get()
    println("js.translator:coreDepsVersion=${coreDepsVersion}")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$coreDepsVersion")
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}
