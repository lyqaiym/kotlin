/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

@file:Suppress("PackageDirectoryMismatch") // Old package for compatibility
package org.jetbrains.kotlin.gradle.plugin.mpp

import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.plugin.hierarchy.KotlinSourceSetTreeClassifier
import org.jetbrains.kotlin.gradle.plugin.hierarchy.KotlinSourceSetTreeClassifier.Property
import org.jetbrains.kotlin.gradle.plugin.hierarchy.sourceSetTreeClassifier
import org.jetbrains.kotlin.gradle.plugin.mpp.compilationImpl.DefaultKotlinCompilationFriendPathsResolver
import org.jetbrains.kotlin.gradle.plugin.mpp.compilationImpl.DefaultKotlinCompilationPreConfigure
import org.jetbrains.kotlin.gradle.plugin.mpp.compilationImpl.KotlinOhosCompilationAssociator
import org.jetbrains.kotlin.gradle.plugin.mpp.compilationImpl.KotlinCompilationLanguageSettingsConfigurator
import org.jetbrains.kotlin.gradle.plugin.mpp.compilationImpl.factory.KotlinCompilationImplFactory
import org.jetbrains.kotlin.gradle.plugin.mpp.compilationImpl.factory.KotlinJvmCompilerOptionsFactory
import org.jetbrains.kotlin.gradle.plugin.mpp.compilationImpl.factory.OhosCompilationDependencyConfigurationsFactory
import org.jetbrains.kotlin.gradle.plugin.mpp.compilationImpl.factory.OhosCompilationSourceSetsContainerFactory
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.type
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.OhosVariantType
import org.jetbrains.kotlin.gradle.utils.*

class KotlinJvmOhosCompilationFactory internal constructor(
    override val target: KotlinOhosTarget,
    @Suppress("TYPEALIAS_EXPANSION_DEPRECATION") private val variant: DeprecatedOhosBaseVariant,
) : KotlinCompilationFactory<KotlinJvmOhosCompilation> {

    override val itemClass: Class<KotlinJvmOhosCompilation>
        get() = KotlinJvmOhosCompilation::class.java

    private val compilationImplFactory: KotlinCompilationImplFactory = KotlinCompilationImplFactory(
        compilerOptionsFactory = KotlinJvmCompilerOptionsFactory,
        compilationDependencyConfigurationsFactory = OhosCompilationDependencyConfigurationsFactory(variant),
        compilationFriendPathsResolver = DefaultKotlinCompilationFriendPathsResolver(
            friendArtifactResolver = DefaultKotlinCompilationFriendPathsResolver.FriendArtifactResolver.composite(
                DefaultKotlinCompilationFriendPathsResolver.DefaultFriendArtifactResolver,
                DefaultKotlinCompilationFriendPathsResolver.AdditionalOhosFriendArtifactResolver
            )
        ),
        compilationAssociator = KotlinOhosCompilationAssociator,
        compilationSourceSetsContainerFactory = OhosCompilationSourceSetsContainerFactory(target, variant),
        preConfigureAction = if (target.isMultiplatformProject) {
            DefaultKotlinCompilationPreConfigure
        } else {
            KotlinCompilationLanguageSettingsConfigurator
        }
    )

    override fun create(name: String): KotlinJvmOhosCompilation {
        return project.objects.KotlinJvmOhosCompilation(
            compilationImplFactory.create(target, name),
            variant,
            variant.javaCompileProvider
        ).also { compilation ->
            configureSourceSetTreeClassifier(compilation)
        }
    }

    private fun configureSourceSetTreeClassifier(compilation: KotlinJvmOhosCompilation) {
        compilation.sourceSetTreeClassifier = when (variant.type) {
            OhosVariantType.Main -> Property(target.mainVariant.sourceSetTree)
            OhosVariantType.UnitTest -> Property(target.unitTestVariant.sourceSetTree)
            OhosVariantType.InstrumentedTest -> Property(target.instrumentedTestVariant.sourceSetTree)
            OhosVariantType.Unknown -> KotlinSourceSetTreeClassifier.None
        }
    }
}

internal class KotlinJvmAgpOhosCompilationFactory(
    private val androidVariantJavaCompileTask: TaskProvider<JavaCompile>,
    private val androidVariantType: OhosVariantType,
    override val target: KotlinOhosTarget,
) : KotlinCompilationFactory<KotlinJvmOhosCompilation> {

    private val compilationImplFactory: KotlinCompilationImplFactory = KotlinCompilationImplFactory(
        compilerOptionsFactory = KotlinJvmCompilerOptionsFactory,
        compilationFriendPathsResolver = DefaultKotlinCompilationFriendPathsResolver(
            friendArtifactResolver = DefaultKotlinCompilationFriendPathsResolver.FriendArtifactResolver.composite(
                DefaultKotlinCompilationFriendPathsResolver.DefaultFriendArtifactResolver,
                DefaultKotlinCompilationFriendPathsResolver.AdditionalOhosFriendArtifactResolver
            )
        ),
        compilationAssociator = KotlinOhosCompilationAssociator,
        preConfigureAction = KotlinCompilationLanguageSettingsConfigurator
    )

    override val itemClass: Class<KotlinJvmOhosCompilation>
        get() = KotlinJvmOhosCompilation::class.java

    override fun create(name: String): KotlinJvmOhosCompilation {
        return project.objects.KotlinJvmOhosCompilation(
            compilationImplFactory.create(target, name),
            null,
            androidVariantJavaCompileTask,
        ).also { compilation ->
            configureSourceSetTreeClassifier(compilation)
        }
    }

    private fun configureSourceSetTreeClassifier(compilation: KotlinJvmOhosCompilation) {
        compilation.sourceSetTreeClassifier = when (androidVariantType) {
            OhosVariantType.Main -> Property(compilation.target.mainVariant.sourceSetTree)
            OhosVariantType.UnitTest -> Property(compilation.target.unitTestVariant.sourceSetTree)
            OhosVariantType.InstrumentedTest -> Property(compilation.target.instrumentedTestVariant.sourceSetTree)
            OhosVariantType.Unknown -> KotlinSourceSetTreeClassifier.None
        }
    }
}
