/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.KotlinJvmPlugin.Companion.configureCompilerOptionsForTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOhosTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinTasksProvider
import org.jetbrains.kotlin.gradle.utils.androidPluginIds
import org.jetbrains.kotlin.gradle.utils.whenEvaluated

internal open class KotlinOhosPlugin() : Plugin<Project> {

    override fun apply(project: Project) {
        project.dynamicallyApplyWhenOhosPluginIsApplied(
            {
                val target = project.objects.KotlinOhosTarget(project)
                val kotlinAndroidExtension = project.kotlinExtension as KotlinOhosProjectExtension
                kotlinAndroidExtension.targetFuture.complete(target)
                project.configureCompilerOptionsForTarget(
                    kotlinAndroidExtension.compilerOptions,
                    target.compilerOptions
                )
                kotlinAndroidExtension.compilerOptions.noJdk.value(true).disallowChanges()

                @Suppress("DEPRECATION") val kotlinOptions = object : KotlinJvmOptions {
                    override val options: KotlinJvmCompilerOptions
                        get() = kotlinAndroidExtension.compilerOptions
                }
                val ext = project.extensions.getByName("ohos") as BaseExtension
                ext.addExtension(KOTLIN_OPTIONS_DSL_NAME, kotlinOptions)
                target
            }
        ) { androidTarget ->
            project.whenEvaluated { project.components.addAll(androidTarget.components) }
        }
    }

    companion object {
        internal fun ohosTargetHandler(): OhosProjectHandler = OhosProjectHandler(KotlinTasksProvider())

        internal fun Project.dynamicallyApplyWhenOhosPluginIsApplied(
            kotlinAndroidTargetProvider: () -> KotlinOhosTarget,
            additionalConfiguration: (KotlinOhosTarget) -> Unit = {}
        ) {
            var wasConfigured = false

            androidPluginIds.forEach { pluginId ->
                plugins.withId(pluginId) {
                    wasConfigured = true
                    val target = kotlinAndroidTargetProvider()
                    ohosTargetHandler().configureTarget(target)
                    additionalConfiguration(target)
                }
            }

            afterEvaluate {
                if (!wasConfigured) {
                    throw GradleException(
                        """
                        |'kotlin-android' plugin requires one of the Android Gradle plugins.
                        |Please apply one of the following plugins to '${project.path}' project:
                        |${androidPluginIds.joinToString(prefix = "- ", separator = "\n\t- ")}
                        """.trimMargin()
                    )
                }
            }
        }
    }
}