/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos.checker

import com.android.Version
import org.jetbrains.kotlin.gradle.plugin.AndroidGradlePluginVersion
import org.jetbrains.kotlin.gradle.plugin.OhosGradlePluginVersion
import org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnostics
import org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector
import org.jetbrains.kotlin.gradle.plugin.diagnostics.reportOncePerGradleBuild
import org.jetbrains.kotlin.gradle.plugin.isAtLeast
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOhosTarget
import org.jetbrains.kotlin.gradle.plugin.sources.android.KotlinAndroidSourceSetLayout
import org.jetbrains.kotlin.gradle.plugin.sources.android.checker.KotlinAndroidSourceSetLayoutChecker
import org.jetbrains.kotlin.gradle.plugin.sources.android.checker.MultiplatformLayoutV2AgpRequirementChecker
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.KotlinOhosSourceSetLayout

internal object MultiplatformOhosLayoutV2AgpRequirementChecker : KotlinOhosSourceSetLayoutChecker {

    internal val minimumRequiredAgpVersion = OhosGradlePluginVersion(7, 0, 0)

    override fun checkBeforeLayoutApplied(
        diagnosticsCollector: KotlinToolingDiagnosticsCollector,
        target: KotlinOhosTarget,
        layout: KotlinOhosSourceSetLayout
    ) {
        if (!isAgpRequirementMet()) {
            diagnosticsCollector.reportOncePerGradleBuild(
                target.project,
                KotlinToolingDiagnostics.AgpRequirementNotMetForAndroidSourceSetLayoutV2(
                    MultiplatformLayoutV2AgpRequirementChecker.minimumRequiredAgpVersion.toString(),
                    Version.ANDROID_GRADLE_PLUGIN_VERSION
                )
            )
        }
    }

    internal fun isAgpRequirementMet(): Boolean {
        return OhosGradlePluginVersion.currentOrNull.isAtLeast(minimumRequiredAgpVersion)
    }
}
