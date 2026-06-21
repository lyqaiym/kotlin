/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:Suppress("DEPRECATION")

package org.jetbrains.kotlin.gradle.utils

import com.android.build.gradle.api.AndroidSourceDirectorySet
import com.android.build.gradle.api.AndroidSourceSet
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.LibraryVariant
import com.android.build.gradle.api.SourceKind
import com.android.build.gradle.api.TestVariant
import com.android.build.gradle.api.TestedComponentIdentifier
import com.android.build.gradle.api.UnitTestVariant

internal typealias DeprecatedOhosSourceSet = AndroidSourceSet
internal typealias DeprecatedOhosSourceDirectorySet = AndroidSourceDirectorySet
internal typealias DeprecatedOhosBaseVariant = BaseVariant
internal typealias DeprecatedOhosApplicationVariant = ApplicationVariant
internal typealias DeprecatedOhosLibraryVariant = LibraryVariant
internal typealias DeprecatedOhosTestVariant = TestVariant
internal typealias DeprecatedOhosUnitTestVariant = UnitTestVariant
internal typealias DeprecatedOhosSourceKind = SourceKind
internal typealias DeprecatedOhosTestedComponentIdentifier = TestedComponentIdentifier
