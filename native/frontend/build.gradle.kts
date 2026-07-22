plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

dependencies {
    compileOnly(project(":compiler:frontend"))
    compileOnly(project(":compiler:frontend.java"))
    implementation(project(":compiler:container"))
    compileOnly(project(":core:descriptors"))
    compileOnly(project(":core:compiler.common.native"))
    compileOnly(project(":compiler:ir.objcinterop"))
    compileOnly(intellijCore())
    api(project(":native:kotlin-native-utils"))
}

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}

standardPublicJars()
