plugins {
    kotlin("jvm")
    //    id("jps-compatible")
//  file:///Users/linyuqiang/github/kotlin_16/js/js.config/build/libs/js.config-2.4.255-SNAPSHOT.jar!/META-INF/org.jetbrains.kotlin_js.config.kotlin_module Module was compiled with an incompatible version of Kotlin.
//  The binary version of its metadata is 2.4.0, expected version is 2.2.0.
    id("gradle-plugin-compiler-dependency-configuration")
//    id("generated-sources")
}

dependencies {
    api(project(":compiler:config"))
    compileOnly(intellijCore())

    compileOnly(project(":core:metadata"))
    embedded(project(":core:metadata")) { isTransitive = false }
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}

//generatedConfigurationKeys("JSConfigurationKeys")
