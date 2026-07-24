plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
//    api(kotlinStdlib())
    val coreDepsVersion = libs.versions.kotlin.`for`.gradle.plugins.compilation.get()
    println("js.ast:coreDepsVersion=${coreDepsVersion}")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:$coreDepsVersion")
    compileOnly(intellijCore())
    compileOnly(libs.intellij.fastutil)
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
