/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos

import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.utils.*

@Suppress("TYPEALIAS_EXPANSION_DEPRECATION")
internal val DeprecatedOhosBaseVariant.type: OhosVariantType
    get() = when (this) {
        is DeprecatedOhosUnitTestVariant -> OhosVariantType.UnitTest
        is DeprecatedOhosTestVariant -> OhosVariantType.InstrumentedTest
        is DeprecatedOhosApplicationVariant, is DeprecatedOhosLibraryVariant -> OhosVariantType.Main
        else -> OhosVariantType.Unknown
    }

internal val OhosBaseSourceSetName.variantType: OhosVariantType
    get() = when (this) {
        OhosBaseSourceSetName.Main -> OhosVariantType.Main
        OhosBaseSourceSetName.Test -> OhosVariantType.UnitTest
        OhosBaseSourceSetName.AndroidTest -> OhosVariantType.InstrumentedTest
    }

// Required for AGP/Built-in Kotlin integration
// ABI preferably should not change
@InternalKotlinGradlePluginApi
enum class OhosVariantType {
    Main, UnitTest, InstrumentedTest, Unknown;
}

/**
 * Every known type of ohos variant has a 'base source set', which
 * participates in all variants of a said type (main, test, androidTest, ...)
 */
internal val OhosVariantType.ohosBaseSourceSetName: OhosBaseSourceSetName?
    get() = when (this) {
        OhosVariantType.Main -> OhosBaseSourceSetName.Main
        OhosVariantType.UnitTest -> OhosBaseSourceSetName.Test
        OhosVariantType.InstrumentedTest -> OhosBaseSourceSetName.AndroidTest
        OhosVariantType.Unknown -> null
    }
