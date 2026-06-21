/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:Suppress("PackageDirectoryMismatch") // Old package for compatibility
package org.jetbrains.kotlin.gradle.plugin.mpp

import org.gradle.api.file.FileCollection
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.plugin.mpp.compilationImpl.KotlinCompilationImpl
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.utils.*
import javax.inject.Inject

internal fun ObjectFactory.KotlinJvmOhosCompilation(
    compilation: KotlinCompilationImpl,
    @Suppress("TYPEALIAS_EXPANSION_DEPRECATION", "UNUSED_PARAMETER") androidVariant: DeprecatedOhosBaseVariant?,
    javaCompileTaskProvider: TaskProvider<JavaCompile>,
): KotlinJvmOhosCompilation = if (androidVariant != null) {
    newInstance(compilation, javaCompileTaskProvider, androidVariant)
} else {
    newInstance<KotlinJvmOhosAgpCompilation>(compilation, javaCompileTaskProvider)
}

/**
 * @param androidVariant AGP `BaseVariant` associated with this compilation. Has `null` value in case of AGP/built-in Kotlin project.
 */
@Suppress("TYPEALIAS_EXPANSION_DEPRECATION", "TYPEALIAS_EXPANSION_DEPRECATION_ERROR", "DEPRECATION")
open class KotlinJvmOhosCompilation @Inject internal constructor(
    compilation: KotlinCompilationImpl,
    javaCompileTaskProvider: TaskProvider<JavaCompile>,
    @Deprecated("Deprecated Ohos Gradle Plugin type which is not available in Kotlin built-in into AGP")
    val androidVariant: DeprecatedOhosBaseVariant?,
) : DeprecatedAbstractKotlinCompilationToRunnableFiles<KotlinAnyOptionsDeprecated>(compilation) {

    override val target: KotlinOhosTarget = compilation.target as KotlinOhosTarget

    @Suppress("DEPRECATION")
    @Deprecated(
        "To configure compilation compiler options use 'compileTaskProvider':\ncompilation.compileTaskProvider.configure{\n" +
                "    compilerOptions {}\n}"
    )
    override val compilerOptions: DeprecatedHasCompilerOptions<KotlinJvmCompilerOptions> =
        compilation.compilerOptions.castCompilerOptionsType()

    internal val testedVariantArtifacts: Property<FileCollection> =
        compilation.project.objects.property(FileCollection::class.java)

    @Suppress("UNCHECKED_CAST")
    override val compileTaskProvider: TaskProvider<out KotlinCompilationTask<KotlinJvmCompilerOptions>>
        get() = compilation.compileTaskProvider as TaskProvider<KotlinCompilationTask<KotlinJvmCompilerOptions>>

    val compileJavaTaskProvider: TaskProvider<out JavaCompile> = javaCompileTaskProvider
}

// Needed for AGP/built-in Kotlin support which does not provide deprecated Android BaseVariant
internal open class KotlinJvmOhosAgpCompilation @Inject constructor(
    compilation: KotlinCompilationImpl,
    javaCompileTaskProvider: TaskProvider<JavaCompile>,
) : KotlinJvmOhosCompilation(compilation, javaCompileTaskProvider, null)
