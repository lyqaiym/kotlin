/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator

import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOhosTarget
import org.jetbrains.kotlin.gradle.utils.*

internal interface KotlinOhosSourceSetConfigurator {
    /**
     * Called once, when the corresponding KotlinSourceSet is created for a given [DeprecatedAndroidSourceSet].
     * Note, this can also be called in 'afterEvaluate', when Android is finalizing its variants.
     */
    fun configure(
        target: KotlinOhosTarget,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") androidSourceSet: DeprecatedOhosSourceSet
    ) = Unit

    /**
     * Called every time, when a given [KotlinSourceSet] participates in a given Android variant.
     */
    fun configureWithVariant(
        target: KotlinOhosTarget,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") variant: DeprecatedOhosBaseVariant
    ) = Unit
}

internal fun KotlinOhosSourceSetConfigurator.onlyIf(
    condition: (target: KotlinOhosTarget) -> Boolean
): KotlinOhosSourceSetConfigurator {
    return KotlinOhosSourceSetConfigurationWithCondition(this, condition)
}

/* Conditional implementation */
private class KotlinOhosSourceSetConfigurationWithCondition(
    private val underlying: KotlinOhosSourceSetConfigurator,
    private val condition: (KotlinOhosTarget) -> Boolean
) : KotlinOhosSourceSetConfigurator {
    override fun configure(
        target: KotlinOhosTarget,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") androidSourceSet: DeprecatedOhosSourceSet
    ) {
        if (condition(target)) underlying.configure(target, kotlinSourceSet, androidSourceSet)
    }

    override fun configureWithVariant(
        target: KotlinOhosTarget,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") variant: DeprecatedOhosBaseVariant
    ) {
        if (condition(target)) underlying.configureWithVariant(target, kotlinSourceSet, variant)
    }
}


/* Composite implementation */

internal fun KotlinOhosSourceSetConfigurator(
    vararg configurators: KotlinOhosSourceSetConfigurator?
): KotlinOhosSourceSetConfigurator {
    return CompositeKotlinOhosSourceSetConfigurator(configurators.filterNotNull())
}

private class CompositeKotlinOhosSourceSetConfigurator(
    val configurators: List<KotlinOhosSourceSetConfigurator>
) : KotlinOhosSourceSetConfigurator {
    override fun configure(
        target: KotlinOhosTarget,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") androidSourceSet: DeprecatedOhosSourceSet
    ) {
        configurators.forEach { configurator ->
            configurator.configure(target, kotlinSourceSet, androidSourceSet)
        }
    }

    override fun configureWithVariant(
        target: KotlinOhosTarget,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") variant: DeprecatedOhosBaseVariant
    ) {
        configurators.forEach { configurator ->
            configurator.configureWithVariant(target, kotlinSourceSet, variant)
        }
    }
}
