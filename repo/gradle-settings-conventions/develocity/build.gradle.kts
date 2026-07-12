import org.jetbrains.kotlin.buildtools.api.ExperimentalBuildToolsApi
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    `kotlin-dsl`
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    maven(url = "https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-dependencies")
    mavenCentral()
    gradlePluginPortal()
}

//:gradle-settings-conventions:jvm-toolchain-provisioning:main: No matching variant of org.gradle.toolchains:foojay-resolver:1.0.0 was found.
//kotlin.jvmToolchain(8)
kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalBuildToolsApi::class)
    compilerVersion = libs.versions.kotlin.`for`.gradle.plugins.compilation
    jvmToolchain(17)
}

//val buildGradlePluginVersion = extra.get("kotlin.build.gradlePlugin.version")
dependencies {
//    implementation("org.jetbrains.kotlin:kotlin-build-gradle-plugin:$buildGradlePluginVersion")
    implementation(kotlinBuildHelpers())
    implementation(libs.develocity.gradlePlugin)
    implementation(libs.gradle.customUserData.gradlePlugin)
}

//:develocity:compileKotlin e: java.lang.NoSuchFieldError: Class org.jetbrains.kotlin.config.CompilerConfigurationKey does not have member field 'org.jetbrains.kotlin.config.CompilerConfigurationKey$Companion Companion'
//at org.jetbrains.kotlin.samWithReceiver.SamWithReceiverConfigurationKeys.<clinit>(SamWithReceiverPlugin.kt:28)

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
    }
}

project.configurations.named(org.jetbrains.kotlin.gradle.plugin.PLUGIN_CLASSPATH_CONFIGURATION_NAME + "Main") {
    resolutionStrategy {
        eachDependency {
            println("gradle-settings-conventions:eachDependency:group=${this.requested.group},n=${this.requested.name},v=${this.requested.version}")
            if (this.requested.group == "org.jetbrains.kotlin") useVersion(libs.versions.kotlin.`for`.gradle.plugins.compilation.get())
        }
    }
}

kotlin.compilerOptions.moduleName.value(project.name)