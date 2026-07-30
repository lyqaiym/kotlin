/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.utils

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.mpp.baseModuleName
import org.jetbrains.kotlin.tooling.core.KotlinToolingVersion

internal fun Project.moduleName(
    baseName: Provider<String> = baseModuleName(),
): Provider<String> = baseName.map {
    moduleName(it)
}

internal fun Project.moduleName(baseName: String = project.name): String =
    if (group.toString().isNotEmpty()) "$group:$baseName" else baseName

internal fun Project.klibModuleName(
    baseName: Provider<String> = baseModuleName(),
): Provider<String> = baseName.map {
    klibModuleName(it)
}

internal fun Project.klibModuleName(baseName: String = project.name): String =
    if (group.toString().isNotEmpty()) "$group:$baseName" else baseName


/**
 * A special handling of the JVM compilations module name to accommodate that it could be
 * compiled with the older Kotlin compiler releases (<2.4.0),
 * which does not have a KT-82216 sanitizes forbidden filename characters fix. In this case we fall back to the old
 * behavior of only using the project name.
 */
internal fun Project.jvmModuleName(
    baseName: Provider<String> = baseModuleName(),
    compilerVersion: Provider<String>,
): Provider<String> = compilerVersion.flatMap { versionString ->
    val version = KotlinToolingVersion(versionString)
    if (version.supportsModuleNameWithGroupPrefix()) moduleName(baseName) else baseName
}

private fun KotlinToolingVersion.supportsModuleNameWithGroupPrefix(): Boolean = major >= 2 && minor >= 4
