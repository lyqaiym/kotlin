/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

internal val Project.kotlinOhosSourceSetLayout: KotlinOhosSourceSetLayout
    get() {
        return if (kotlinExtension is KotlinMultiplatformExtension) {
            multiplatformOhosSourceSetLayoutV2
        } else singleTargetOhosSourceSetLayout
    }
