plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    implementation(project(":core:metadata"))
    implementation(project(":core:deserialization.common"))
    implementation(project(":core:compiler.common"))

    api(project(":compiler:fir:cones"))
    api(project(":compiler:fir:tree"))
    api(project(":compiler:fir:providers"))
    api(project(":compiler:fir:semantics"))

    compileOnly(intellijCore())
}

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}
