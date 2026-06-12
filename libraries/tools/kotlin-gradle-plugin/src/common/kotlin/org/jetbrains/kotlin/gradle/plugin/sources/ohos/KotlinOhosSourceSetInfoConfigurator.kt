/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos

import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOhosTarget
import org.jetbrains.kotlin.gradle.plugin.sources.android.type
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.KotlinOhosSourceSetConfigurator
import org.jetbrains.kotlin.gradle.utils.*

internal object KotlinOhosSourceSetInfoConfigurator : KotlinOhosSourceSetConfigurator {
    override fun configureWithVariant(
        target: KotlinOhosTarget,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") variant: DeprecatedOhosBaseVariant
    ) {
        val info = kotlinSourceSet.ohosSourceSetInfo.asMutable()
        info.androidVariantType = variant.type
        info.androidVariantNames.add(variant.name)
    }
}
