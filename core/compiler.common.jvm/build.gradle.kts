plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("gradle-plugin-compiler-dependency-configuration")
}

project.configureJvmToolchain(JdkMajorVersion.JDK_1_8)

dependencies {
    api(project(":core:compiler.common"))
    implementation(project(":core:descriptors"))
    implementation(project(":compiler:frontend"))
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
