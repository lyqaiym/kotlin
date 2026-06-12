/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos

import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.utils.*

@Suppress("TYPEALIAS_EXPANSION_DEPRECATION")
internal val NamedDomainObjectContainer<out DeprecatedAndroidSourceSet>.main: DeprecatedAndroidSourceSet
    get() = getByName(OhosBaseSourceSetName.Main.name)

@Suppress("TYPEALIAS_EXPANSION_DEPRECATION")
internal val NamedDomainObjectContainer<out DeprecatedAndroidSourceSet>.test: DeprecatedAndroidSourceSet
    get() = getByName(OhosBaseSourceSetName.Test.name)

@Suppress("TYPEALIAS_EXPANSION_DEPRECATION")
internal val NamedDomainObjectContainer<out DeprecatedAndroidSourceSet>.androidTest: DeprecatedAndroidSourceSet
    get() = getByName(OhosBaseSourceSetName.AndroidTest.name)

/*
Not written as enum class to avoid Enum.name ambiguity with 'source set name' semantics.
 */
internal sealed class OhosBaseSourceSetName(val name: String) {
    final override fun toString(): String = name

    object Main : OhosBaseSourceSetName("main")
    object Test : OhosBaseSourceSetName("test")
    object AndroidTest : OhosBaseSourceSetName("androidTest")

    companion object {
        fun byName(name: String): OhosBaseSourceSetName? = when (name) {
            Main.name -> Main
            Test.name -> Test
            AndroidTest.name -> AndroidTest
            else -> null
        }
    }
}
