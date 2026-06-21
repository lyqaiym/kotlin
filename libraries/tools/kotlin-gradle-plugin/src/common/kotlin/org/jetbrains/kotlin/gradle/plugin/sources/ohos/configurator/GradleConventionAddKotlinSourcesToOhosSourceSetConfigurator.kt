/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator

import org.jetbrains.kotlin.gradle.plugin.KOTLIN_DSL_NAME
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.internal.compatibilityConventionRegistrar
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOhosTarget
import org.jetbrains.kotlin.gradle.plugin.sources.android.configurator.KotlinAndroidSourceSetConfigurator
import org.jetbrains.kotlin.gradle.utils.*

/*
Used by AGP com.android.build.gradle.internal.utils.syncAgpAndKgpSources to automatically pick up sources
from this KotlinSourceSet. This mechanism is deprecated.
*/
internal object GradleConventionAddKotlinSourcesToOhosSourceSetConfigurator : KotlinOhosSourceSetConfigurator {
    override fun configure(
        target: KotlinOhosTarget,
        kotlinSourceSet: KotlinSourceSet,
        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") androidSourceSet: DeprecatedOhosSourceSet
    ) {
        target.project.compatibilityConventionRegistrar.addConvention(androidSourceSet, KOTLIN_DSL_NAME, kotlinSourceSet)
    }
}
