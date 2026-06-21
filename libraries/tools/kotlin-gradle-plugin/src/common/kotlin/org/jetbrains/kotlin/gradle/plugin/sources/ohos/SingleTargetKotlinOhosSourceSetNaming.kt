/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos

import org.jetbrains.kotlin.gradle.plugin.sources.android.AndroidVariantType
import org.jetbrains.kotlin.gradle.plugin.sources.android.KotlinAndroidSourceSetNaming
import org.jetbrains.kotlin.gradle.utils.lowerCamelCaseName

internal object SingleTargetKotlinOhosSourceSetNaming : KotlinOhosSourceSetNaming {
    override fun kotlinSourceSetName(disambiguationClassifier: String, androidSourceSetName: String, type: OhosVariantType?): String {
        assert(disambiguationClassifier.isEmpty()) { "Unexpected non-empty disambiguationClassifier found: $disambiguationClassifier" }
        return lowerCamelCaseName(disambiguationClassifier, androidSourceSetName)
    }
}
