/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:Suppress("DuplicatedCode", "FunctionName")

package org.jetbrains.kotlin.gradle.plugin

import com.android.Version
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi
import java.io.Serializable
import java.util.*

@InternalKotlinGradlePluginApi
fun OhosGradlePluginVersion(versionString: String): OhosGradlePluginVersion {
    return OhosGradlePluginVersionOrNull(versionString)
        ?: throw IllegalArgumentException("Invalid Android Gradle Plugin version: $versionString")
}

internal fun OhosGradlePluginVersionOrNull(versionString: String): OhosGradlePluginVersion? {
    val baseVersion = versionString.split("-", limit = 2)[0]
    val classifier = versionString.split("-", limit = 2).getOrNull(1)

    val baseVersionSplit = baseVersion.split(".")
    if (!(baseVersionSplit.size == 2 || baseVersionSplit.size == 3)) return null

    return OhosGradlePluginVersion(
        major = baseVersionSplit[0].toIntOrNull() ?: return null,
        minor = baseVersionSplit[1].toIntOrNull() ?: return null,
        patch = baseVersionSplit.getOrNull(2)?.let { it.toIntOrNull() ?: return null } ?: 0,
        classifier = classifier
    )
}

@InternalKotlinGradlePluginApi
data class OhosGradlePluginVersion(
    val major: Int,
    val minor: Int,
    val patch: Int = 0,
    val classifier: String? = null
) : Comparable<OhosGradlePluginVersion>, Serializable {
    override fun compareTo(other: OhosGradlePluginVersion): Int {
        if (this === other) return 0
        (this.major - other.major).takeIf { it != 0 }?.let { return it }
        (this.minor - other.minor).takeIf { it != 0 }?.let { return it }
        (this.patch - other.patch).takeIf { it != 0 }?.let { return it }

        if (this.classifier == null && other.classifier == null) return 0
        if (this.classifier == null) return 1
        if (other.classifier == null) return -1

        val thisClassifierLowercase = this.classifier.lowercase()
        val otherClassifierLowercase = other.classifier.lowercase()
        if (thisClassifierLowercase == otherClassifierLowercase) return 0
        return thisClassifierLowercase.compareTo(otherClassifierLowercase)
    }

    override fun toString(): String {
        return "$major.$minor.$patch" + if (classifier != null) "-$classifier" else ""
    }

    companion object {
        val currentOrNull: OhosGradlePluginVersion? = try {
            OhosGradlePluginVersion(Version.ANDROID_GRADLE_PLUGIN_VERSION)
        } catch (_: LinkageError) {
            null
        }

        /**
         * The currently applied/accessible Android Gradle Plugin version
         */
        val current: OhosGradlePluginVersion
            get() = currentOrNull ?: throw IllegalStateException(
                "Can't infer current OhosGradlePluginVersion: Is the Android plugin applied?"
            )
    }
}

internal operator fun OhosGradlePluginVersion.compareTo(versionString: String): Int {
    return this.compareTo(OhosGradlePluginVersion(versionString))
}

internal fun OhosGradlePluginVersion?.isAtLeast(versionString: String): Boolean {
    if (this == null) return false
    return this >= OhosGradlePluginVersion(versionString)
}

internal fun OhosGradlePluginVersion?.isAtLeast(version: OhosGradlePluginVersion): Boolean {
    if (this == null) return false
    return this >= version
}
