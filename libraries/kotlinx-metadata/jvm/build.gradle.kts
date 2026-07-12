description = "Kotlin JVM metadata manipulation library"
group = "org.jetbrains.kotlin"

plugins {
    kotlin("jvm")
    //    id("jps-compatible")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("org.jetbrains.dokka")
}


sourceSets {
    "main" { projectDefault() }
    "test" { projectDefault() }
}

val embedded by configurations
embedded.isTransitive = false
configurations.getByName("compileOnly").extendsFrom(embedded)
configurations.getByName("testApi").extendsFrom(embedded)

dependencies {
    api(kotlinStdlib())
    embedded(project(":kotlin-metadata"))
    embedded(project(":core:metadata"))
    embedded(project(":core:metadata.jvm"))
    embedded(protobufLite())
    testImplementation(kotlinTest("junit5"))
    testImplementation(libs.intellij.asm)
    testImplementation(commonDependency("org.jetbrains.kotlin:kotlin-reflect")) { isTransitive = false }
}

kotlin {
    explicitApi()
    compilerOptions {
        freeCompilerArgs.add("-Xallow-kotlin-package")
    }
}

//projectTest(jUnitMode = JUnitMode.JUnit5) {
//    useJUnitPlatform()
//}

publish()

val unshaded by task<Jar> {
    archiveClassifier.set("unshaded")
    from(mainSourceSet.output)
}
project.addArtifact("unshaded", unshaded, unshaded)

val runtimeJar = runtimeJarWithRelocation {
    from(mainSourceSet.output)
    exclude("**/*.proto")
    relocate("org.jetbrains.kotlin", "kotlin.metadata.internal")
}.apply {
    configure {
        manifest {
            attributes("Automatic-Module-Name" to "kotlin.metadata.jvm")
        }
    }
}

tasks.apiBuild {
    inputJar.value(runtimeJar.flatMap { it.archiveFile })
}

apiValidation {
    ignoredPackages.add("kotlin.metadata.internal")
    nonPublicMarkers.addAll(
        listOf(
            "kotlin.metadata.internal.IgnoreInApiDump",
            "kotlin.metadata.jvm.internal.IgnoreInApiDump"
        )
    )
}

dokka {
    dokkaGeneratorIsolation = ProcessIsolation {
        // enable support for kotlin package - required with K2 analysis
        systemProperties.put("org.jetbrains.dokka.analysis.allowKotlinPackage", "true")
    }

    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("dokka"))
        failOnWarning.set(true)
    }
    pluginsConfiguration.html {
        templatesDir.set(projectDir.resolve("dokka-templates"))
    }

    dokkaSourceSets.configureEach {
        includes.from(project.file("dokka/moduledoc.md").path)

        sourceRoots.from(project(":kotlin-metadata").getSources())

        skipDeprecated.set(true)
        reportUndocumented.set(true)

        perPackageOption {
            matchingRegex.set("kotlin\\.metadata\\.internal(\$|\\.).*")
            suppress.set(true)
            reportUndocumented.set(false)
        }
    }
}

sourcesJar()

javadocJar()
