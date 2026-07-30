/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.diagnostics

import java.util.*

/**
 * Represents a grouping mechanism for diagnostics, which helps categorize diagnostics
 * based on logical or hierarchical groupings.
 *
 * @property groupId A unique identifier for the diagnostic group.
 * @property displayName A user-friendly name of the diagnostic group.
 * @property groupPath The fully qualified path of the diagnostic group, which may reflect
 * hierarchical nesting of groups.
 * @property parent The parent diagnostic group of this group, or null if this group has
 * no parent and is at the top level.
 *
 * @since 2.2.0
 */
internal sealed interface DiagnosticGroup {
    val groupId: String
    val displayName: String
    val groupPath: String
    val parent: DiagnosticGroup?

    /**
     * Base implementation for DiagnosticGroup that provides common functionality.
     */
    abstract class Base : DiagnosticGroup {
        override fun toString() = "$groupId | $displayName | parent: [$parent]"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is DiagnosticGroup) return false
            return groupId == other.groupId && parent == other.parent
        }

        override fun hashCode(): Int = Objects.hash(groupId, parent)
    }

    /**
     * Represents a diagnostic group for generic Kotlin related diagnostics.
     */
    object KotlinDiagnosticGroup : Base() {
        override val groupId: String = GroupId.KOTLIN
        override val displayName: String = "Kotlin"
        override val parent: DiagnosticGroup? = null
        override val groupPath: String = groupId.lowercase(Locale.getDefault())
    }

    /**
     * Represents a hierarchical structure of diagnostic groups related to compiler diagnostics.
     */
    sealed class Compiler(
        private val category: String? = null,
    ) : Base() {
        override val groupId: String = when (category) {
            Category.ERROR, Category.WARNING -> "${GroupId.COMPILER}:$category"
            else -> GroupId.COMPILER
        }

        override val parent: DiagnosticGroup = KotlinDiagnosticGroup

        override val displayName: String
            get() = buildString {
                append("Kotlin Compiler")
                category?.let {
                    append(" ${Category.getDisplayName(it)}")
                }
            }

        override val groupPath: String = buildString {
            append(parent.groupPath)
            append(":")
            append(groupId.lowercase(Locale.getDefault()))
        }

        private object Category {
            const val ERROR = "ERROR"
            const val WARNING = "WARNING"

            fun getDisplayName(category: String): String = when (category) {
                ERROR -> "Error"
                WARNING -> "Warning"
                else -> throw IllegalArgumentException("Unknown category: $category")
            }
        }

        object Default : Compiler()
        object Error : Compiler(Category.ERROR)
        object Warning : Compiler(Category.WARNING)
    }
}

/**
 * Provides access to predefined diagnostic groups for the Kotlin Gradle Plugin (KGP).
 *
 * The `DiagnosticGroups` object organizes diagnostic groups under categories such as `kotlin`, `compose`,
 * and subcategories within the `KGP` object to facilitate granular management of diagnostics.
 *
 * These diagnostic groups are used to categorize and structure diagnostic messages hierarchically
 * throughout the Kotlin Gradle Plugin.
 *
 * @since 2.2.0
 */
internal object DiagnosticGroups {
    /**
     * Represents a diagnostic group for generic Kotlin related diagnostics.
     */
    val Kotlin: DiagnosticGroup get() = ToolingDiagnosticGroup.KotlinDiagnosticGroup

    /**
     * Represents a diagnostic group for Kotlin Gradle Plugin (KGP).
     * These diagnostic groups are categorized to group diagnostics logically.
     *
     * - Default: Represents the default diagnostic group for KGP.
     * - Deprecation: Represents the diagnostic group for deprecation warnings or notifications.
     * - Misconfiguration: Represents the diagnostic group for configuration-related issues in KGP.
     * - Experimental: Represents the diagnostic group for experimental features in KGP.
     */
    object KGP {
        val Default: DiagnosticGroup get() = ToolingDiagnosticGroup.KGPDiagnosticGroup.Default
        val Deprecation: DiagnosticGroup get() = ToolingDiagnosticGroup.KGPDiagnosticGroup.Deprecation
        val Misconfiguration: DiagnosticGroup get() = ToolingDiagnosticGroup.KGPDiagnosticGroup.Misconfiguration
        val Experimental: DiagnosticGroup get() = ToolingDiagnosticGroup.KGPDiagnosticGroup.Experimental
    }
}

/**
 * Holds constants representing group identifiers for diagnostic messages within the Kotlin Gradle Plugin.
 *
 * These group identifiers are used to classify and organize diagnostics,
 * ensuring they are correctly grouped and identifiable by their categories.
 *
 * @since 2.2.0
 */
//private object GroupId {
//    const val KOTLIN = "KOTLIN"
//    const val KGP = "KGP"
//}

/**
 * Represents a hierarchical structure of diagnostic groups related to tooling in the Kotlin Gradle Plugin.
 * This sealed class provides predefined diagnostic groups and allows extension for new diagnostic subgroups.
 *
 * @property groupId A unique identifier for the diagnostic group.
 * @property parent The parent diagnostic group, if any.
 *
 * @since 2.2.0
 */
private sealed class ToolingDiagnosticGroup private constructor(
    override val groupId: String,
    override val parent: DiagnosticGroup? = null
) : DiagnosticGroup {

    abstract override val displayName: String

    override fun toString() = "$groupId | $displayName | parent: [$parent]"

    object KotlinDiagnosticGroup : ToolingDiagnosticGroup(GroupId.KOTLIN) {
        override val displayName: String = "Kotlin"
    }

    class KGPDiagnosticGroup private constructor(
        private val category: String? = null
    ) : ToolingDiagnosticGroup(
        when (category) {
            Category.DEPRECATION, Category.MISCONFIGURATION, Category.EXPERIMENTAL -> "${GroupId.KGP}:$category"
            else -> GroupId.KGP
        },
        KotlinDiagnosticGroup
    ) {

        private object Category {
            const val DEPRECATION = "DEPRECATION"
            const val MISCONFIGURATION = "MISCONFIGURATION"
            const val EXPERIMENTAL = "EXPERIMENTAL"

            fun getDisplayName(category: String): String = when (category) {
                DEPRECATION -> "Deprecation"
                MISCONFIGURATION -> "Misconfiguration"
                EXPERIMENTAL -> "Experimental Feature"
                else -> throw IllegalArgumentException("Unknown category: $category")
            }
        }

        override val displayName: String
            get() = buildString {
                append("Kotlin Gradle Plugin")
                category?.let {
                    append(" ${Category.getDisplayName(it)}")
                }
            }

        companion object {
            val Default = KGPDiagnosticGroup()
            val Deprecation = KGPDiagnosticGroup(Category.DEPRECATION)
            val Misconfiguration = KGPDiagnosticGroup(Category.MISCONFIGURATION)
            val Experimental = KGPDiagnosticGroup(Category.EXPERIMENTAL)
        }
    }

    override val groupPath: String = buildString {
        parent?.let {
            append(it.groupPath)
            append(":")
        }
        append(groupId.lowercase(Locale.getDefault()))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DiagnosticGroup) return false
        return groupId == other.groupId && parent == other.parent
    }

    override fun hashCode(): Int = Objects.hash(groupId, parent)
}

/**
 * Holds constants representing group identifiers for diagnostic messages within the Kotlin Gradle Plugin.
 *
 * These group identifiers are used to classify and organize diagnostics,
 * ensuring they are correctly grouped and identifiable by their categories.
 *
 * @since 2.2.0
 */
public object GroupId {
    const val KOTLIN = "KOTLIN"
    const val KGP = "KGP"
    const val COMPILER = "COMPILER"
}