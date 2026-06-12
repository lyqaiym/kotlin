/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos.checker

import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.diagnostics.KotlinToolingDiagnosticsCollector
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOhosTarget
import org.jetbrains.kotlin.gradle.plugin.sources.android.KotlinAndroidSourceSetLayout
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.KotlinOhosSourceSetLayout
import org.jetbrains.kotlin.gradle.utils.*

internal interface KotlinOhosSourceSetLayoutChecker {
    fun checkBeforeLayoutApplied(
        diagnosticsCollector: KotlinToolingDiagnosticsCollector,
        target: KotlinOhosTarget,
        layout: KotlinOhosSourceSetLayout
    ) = Unit

    fun checkCreatedSourceSet(
        diagnosticsCollector: KotlinToolingDiagnosticsCollector,
        target: KotlinOhosTarget,
        layout: KotlinOhosSourceSetLayout,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") androidSourceSet: DeprecatedOhosSourceSet
    ) = Unit
}

/* Composite Implementation */

internal fun KotlinOhosSourceSetLayoutChecker(
    vararg checkers: KotlinOhosSourceSetLayoutChecker?
): KotlinOhosSourceSetLayoutChecker {
    return CompositeKotlinOhosSourceSetLayoutChecker(checkers.filterNotNull())
}

private class CompositeKotlinOhosSourceSetLayoutChecker(
    private val checkers: List<KotlinOhosSourceSetLayoutChecker>
) : KotlinOhosSourceSetLayoutChecker {

    override fun checkBeforeLayoutApplied(
        diagnosticsCollector: KotlinToolingDiagnosticsCollector,
        target: KotlinOhosTarget,
        layout: KotlinOhosSourceSetLayout
    ) {
        checkers.forEach { checker -> checker.checkBeforeLayoutApplied(diagnosticsCollector, target, layout) }
    }

    override fun checkCreatedSourceSet(
        diagnosticsCollector: KotlinToolingDiagnosticsCollector,
        target: KotlinOhosTarget,
        layout: KotlinOhosSourceSetLayout,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") androidSourceSet: DeprecatedOhosSourceSet
    ) {
        checkers.forEach { checker ->
            checker.checkCreatedSourceSet(diagnosticsCollector, target, layout, kotlinSourceSet, androidSourceSet)
        }
    }
}
