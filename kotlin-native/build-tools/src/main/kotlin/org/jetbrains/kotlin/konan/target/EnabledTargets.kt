/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.konan.target

import org.jetbrains.kotlin.utils.KotlinLogger

fun enabledTargets(platformManager: PlatformManager) = platformManager.enabled.filterNot {
    KotlinLogger.warning("enabledTargets:it=${it}")
    it in KonanTarget.deprecatedTargets && it !in KonanTarget.toleratedDeprecatedTargets
}
