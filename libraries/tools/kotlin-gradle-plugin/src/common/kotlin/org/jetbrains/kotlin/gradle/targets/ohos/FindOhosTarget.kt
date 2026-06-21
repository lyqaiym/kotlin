/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.targets.ohos

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinOhosProjectExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinOhosTarget

internal fun Project.findOhosTarget(): KotlinOhosTarget? {
    return when (val kotlinExtension = project.kotlinExtension) {
        is KotlinMultiplatformExtension -> kotlinExtension.targets.withType(KotlinOhosTarget::class.java).singleOrNull()
        is KotlinOhosProjectExtension -> kotlinExtension.target
        else -> null
    }
}
