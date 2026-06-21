/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
@file:Suppress("PackageDirectoryMismatch") // Old package for compatibility

package org.jetbrains.kotlin.gradle.plugin.mpp

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.utils.property


@ExperimentalKotlinGradlePluginApi
interface KotlinOhosTargetVariantDsl {
    /**
     * Configures under which [KotlinSourceSetTree] the currently configured Ohos Variant shall be placed.
     * e.g.
     *
     * ```kotlin
     * kotlin {
     *     OhosTarget().instrumentedTest {
     *         sourceSetTree.set(SourceSetTree.test)
     *     }
     * }
     * ```
     *
     * Will ensure that all ohos instrumented tests (OhosInstrumentedTest, ohosInstrumentedTestDebug, ...)
     * will be placed into the 'test' SourceSet tree (with 'commonTest' as root)
     */
    val sourceSetTree: Property<KotlinSourceSetTree>
}

internal class KotlinOhosTargetVariantDslImpl(objects: ObjectFactory) : KotlinOhosTargetVariantDsl {
    override val sourceSetTree: Property<KotlinSourceSetTree> = objects.property()
}
