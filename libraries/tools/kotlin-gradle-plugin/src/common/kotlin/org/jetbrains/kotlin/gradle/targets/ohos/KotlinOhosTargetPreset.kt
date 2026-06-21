/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:Suppress("PackageDirectoryMismatch") // Old package for compatibility
package org.jetbrains.kotlin.gradle.plugin.mpp

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.internal.diagnostics.AgpWithBuiltInKotlinAppliedCheck.checkIfNewDslIsUsed
import org.jetbrains.kotlin.gradle.internal.diagnostics.AgpWithBuiltInKotlinAppliedCheck.reportKotlinAndroidDeprecation
import org.jetbrains.kotlin.gradle.plugin.KotlinOhosPlugin.Companion.dynamicallyApplyWhenOhosPluginIsApplied
import org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnostics
import org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnostics.AndroidGradlePluginIsMissing
import org.jetbrains.kotlin.gradle.plugin.diagnostics.reportDiagnostic
import org.jetbrains.kotlin.gradle.targets.android.internal.InternalKotlinTargetPreset
import org.jetbrains.kotlin.gradle.utils.findAppliedOhosPluginIdOrNull
import org.jetbrains.kotlin.util.DummyLogger

import javax.inject.Inject

internal abstract class KotlinOhosTargetPreset @Inject constructor(
    private val project: Project
) : InternalKotlinTargetPreset<KotlinOhosTarget> {

//    override val name: String = PRESET_NAME

    override fun createTargetInternal(name: String): KotlinOhosTarget {
        val logger = DummyLogger
        logger.warning("createTargetInternal:name=${name}")
        /*
        Android Gradle Plugin is required:
        Creating target will fail with Linkage Error instead

        Could not create an instance of type org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget.
            > Could not generate a decorated class for type KotlinAndroidTarget.
            > com/android/build/gradle/api/BaseVariant
         */
        val androidPluginId = project.findAppliedOhosPluginIdOrNull()
        if (androidPluginId == null) {
            project.reportDiagnostic(AndroidGradlePluginIsMissing(Throwable()))
        } else {
            project.checkIfNewDslIsUsed(isKmpProject = true)
            project.reportKotlinAndroidDeprecation(
                KotlinToolingDiagnostics.NonKmpAgpIsDeprecated(androidPluginId)
            )
        }

        return project.objects.KotlinOhosTarget(project, name, true).apply {
            preset = this@KotlinOhosTargetPreset
            project.dynamicallyApplyWhenOhosPluginIsApplied({ this })
        }
    }

    companion object {
        const val PRESET_NAME = "ohos"
    }
}
