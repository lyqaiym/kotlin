/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos

import org.gradle.api.logging.Logging
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOhosTarget
import org.jetbrains.kotlin.gradle.plugin.sources.android.AndroidBaseSourceSetName
import org.jetbrains.kotlin.gradle.plugin.sources.android.AndroidVariantType
import org.jetbrains.kotlin.gradle.plugin.sources.android.KotlinAndroidSourceSetNaming
import org.jetbrains.kotlin.gradle.plugin.sources.android.androidBaseSourceSetName
import org.jetbrains.kotlin.gradle.plugin.sources.android.findKotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.sources.android.type
import org.jetbrains.kotlin.gradle.plugin.sources.android.variantType
import org.jetbrains.kotlin.gradle.utils.*

internal object MultiplatformLayoutV2KotlinOhosSourceSetNaming : KotlinOhosSourceSetNaming {
    private val logger = Logging.getLogger(this::class.java)

    private val OhosBaseSourceSetName.kotlinName
        get() = when (this) {
            OhosBaseSourceSetName.Main -> "main"
            OhosBaseSourceSetName.Test -> "unitTest"
            OhosBaseSourceSetName.AndroidTest -> "instrumentedTest"
        }

    override fun kotlinSourceSetName(
        disambiguationClassifier: String,
        androidSourceSetName: String,
        type: OhosVariantType?
    ): String? {
        val knownType = type ?: OhosBaseSourceSetName.Companion.byName(androidSourceSetName)?.variantType ?: return null
        return lowerCamelCaseName(disambiguationClassifier, replaceOhosBaseSourceSetName(androidSourceSetName, knownType))
    }

    override fun defaultKotlinSourceSetName(
        target: KotlinOhosTarget,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") variant: DeprecatedOhosBaseVariant
    ): String? {
        val kotlinSourceSetName: String? = run {
            val baseSourceSetName = variant.type.androidBaseSourceSetName ?: return@run null
            val androidSourceSetName = lowerCamelCaseName(
                baseSourceSetName.takeIf { it != AndroidBaseSourceSetName.Main }?.name,
                variant.flavorName,
                variant.buildType.name
            )
            val androidSourceSet = variant.sourceSets.find { it.name == androidSourceSetName } ?: return@run null
            target.project.findKotlinSourceSet(androidSourceSet)?.name
        }

        if (kotlinSourceSetName == null) {
            logger.warn("Can't determine 'defaultKotlinSourceSet' for android compilation: ${variant.name}")
        }
        return kotlinSourceSetName
    }

    private fun replaceOhosBaseSourceSetName(
        androidSourceSetName: String,
        type: OhosVariantType
    ): String {
        if (type == OhosVariantType.Main) return androidSourceSetName
        val androidBaseSourceSetName = type.ohosBaseSourceSetName ?: return androidSourceSetName
        return lowerCamelCaseName(androidBaseSourceSetName.kotlinName, androidSourceSetName.removePrefix(androidBaseSourceSetName.name))
    }
}
