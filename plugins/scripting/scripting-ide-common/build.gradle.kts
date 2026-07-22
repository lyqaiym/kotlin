plugins {
    kotlin("jvm")
    //    id("jps-compatible")
}

jvmTarget = "1.8"

dependencies {
    compileOnly(project(":compiler:util"))
    implementation(project(":core:descriptors"))
    compileOnly(project(":compiler:frontend"))
    compileOnly(project(":compiler:frontend.java"))
    compileOnly(project(":js:js.frontend"))
    implementation(project(":compiler:resolution"))
    implementation(project(":core:descriptors.jvm"))
    compileOnly(intellijCore())

}

sourceSets {
    "main" { projectDefault() }
    "test" { }
}
