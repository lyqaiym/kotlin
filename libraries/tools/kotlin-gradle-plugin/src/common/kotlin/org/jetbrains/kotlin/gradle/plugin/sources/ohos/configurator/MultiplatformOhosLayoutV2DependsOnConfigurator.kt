/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator

import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.dsl.multiplatformExtensionOrNull
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginLifecycle
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.awaitFinalValue
import org.jetbrains.kotlin.gradle.plugin.launchInStage
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOhosTarget
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.type
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.OhosBaseSourceSetName
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.OhosVariantType
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.variantType
import org.jetbrains.kotlin.gradle.utils.*

internal object MultiplatformOhosLayoutV2DependsOnConfigurator : KotlinOhosSourceSetConfigurator {
    override fun configure(
        target: KotlinOhosTarget,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") androidSourceSet: DeprecatedOhosSourceSet
    ) {
        val androidBaseSourceSetName = OhosBaseSourceSetName.byName(androidSourceSet.name) ?: return
        setDefaultDependsOn(target, kotlinSourceSet, androidBaseSourceSetName.variantType)
    }

    override fun configureWithVariant(
        target: KotlinOhosTarget,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") variant: DeprecatedOhosBaseVariant
    ) {
        setDefaultDependsOn(target, kotlinSourceSet, variant.type)
    }

    private fun setDefaultDependsOn(target: KotlinOhosTarget, kotlinSourceSet: KotlinSourceSet, variantType: OhosVariantType) {
        target.project.launchInStage(KotlinPluginLifecycle.Stage.FinaliseRefinesEdges) {
            /* Only setup default if no hierarchy template was applied */
            if (target.project.multiplatformExtensionOrNull?.hierarchy?.appliedTemplates.orEmpty().isNotEmpty()) {
                return@launchInStage
            }

            val sourceSetTree = when (variantType) {
                OhosVariantType.Main -> target.mainVariant.sourceSetTree.awaitFinalValue()
                OhosVariantType.UnitTest -> target.unitTestVariant.sourceSetTree.awaitFinalValue()
                OhosVariantType.InstrumentedTest -> target.instrumentedTestVariant.sourceSetTree.awaitFinalValue()
                OhosVariantType.Unknown -> null
            } ?: return@launchInStage

            val commonSourceSetName = lowerCamelCaseName("common", sourceSetTree.name)
            val commonSourceSet = target.project.kotlinExtension.sourceSets.findByName(commonSourceSetName) ?: return@launchInStage
            kotlinSourceSet.dependsOn(commonSourceSet)
        }
    }
}
