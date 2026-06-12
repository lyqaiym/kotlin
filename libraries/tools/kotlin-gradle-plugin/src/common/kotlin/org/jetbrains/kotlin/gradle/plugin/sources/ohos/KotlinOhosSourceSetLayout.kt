/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.sources.ohos

import org.jetbrains.kotlin.gradle.plugin.AndroidGradlePluginVersion
import org.jetbrains.kotlin.gradle.plugin.OhosGradlePluginVersion
import org.jetbrains.kotlin.gradle.plugin.compareTo
import org.jetbrains.kotlin.gradle.plugin.sources.android.checker.*
import org.jetbrains.kotlin.gradle.plugin.sources.android.configurator.*
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.checker.KotlinOhosSourceSetLayoutChecker
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.checker.MultiplatformOhosLayoutV2AgpRequirementChecker
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.checker.MultiplatformOhosLayoutV2AndroidStyleSourceDirUsageChecker
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.Agp7AddKotlinSourcesToOhosSourceSetConfigurator
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.GradleConventionAddKotlinSourcesToOhosSourceSetConfigurator
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.KotlinOhosJavaSourceDirConfigurator
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.KotlinOhosSourceSetConfigurator
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.MultiplatformOhosLayoutV2DefaultManifestLocationConfigurator
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.MultiplatformOhosLayoutV2DependsOnConfigurator
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.MultiplatformOhosLayoutV2SourceDirConfigurator
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.MultiplatformOhosResourceDirConfigurator
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.OhosKaptSourceSetConfigurator
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.SingleOhosTargetSourceDirConfigurator
import org.jetbrains.kotlin.gradle.plugin.sources.ohos.configurator.onlyIf

internal data class KotlinOhosSourceSetLayout(
    val name: String,
    val naming: KotlinOhosSourceSetNaming,
    val sourceSetConfigurator: KotlinOhosSourceSetConfigurator,
    val checker: KotlinOhosSourceSetLayoutChecker
) {
    override fun toString(): String = "KotlinAndroidSourceSetLayout: $name"
}

internal val singleTargetOhosSourceSetLayout = KotlinOhosSourceSetLayout(
    name = "Kotlin/Android-SourceSetLayout",
    naming = SingleTargetKotlinOhosSourceSetNaming,
    sourceSetConfigurator = KotlinOhosSourceSetConfigurator(
        KotlinOhosSourceSetInfoConfigurator,
        OhosKaptSourceSetConfigurator,
        GradleConventionAddKotlinSourcesToOhosSourceSetConfigurator,
        Agp7AddKotlinSourcesToOhosSourceSetConfigurator
            .onlyIf { OhosGradlePluginVersion.current >= "7.0.0" },
        KotlinOhosJavaSourceDirConfigurator,
        SingleOhosTargetSourceDirConfigurator,
    ),
    checker = KotlinOhosSourceSetLayoutChecker()
)

internal val multiplatformOhosSourceSetLayoutV2 = KotlinOhosSourceSetLayout(
    name = "Multiplatform/Android-V2-SourceSetLayout",
    naming = MultiplatformLayoutV2KotlinOhosSourceSetNaming,
    sourceSetConfigurator = KotlinOhosSourceSetConfigurator(
        KotlinOhosSourceSetInfoConfigurator,
        OhosKaptSourceSetConfigurator,
        MultiplatformOhosResourceDirConfigurator,
        MultiplatformOhosLayoutV2DependsOnConfigurator,
        Agp7AddKotlinSourcesToOhosSourceSetConfigurator
            .onlyIf { OhosGradlePluginVersion.current >= "7.0.0" },
        KotlinOhosJavaSourceDirConfigurator,
        MultiplatformOhosLayoutV2SourceDirConfigurator,
        MultiplatformOhosLayoutV2DefaultManifestLocationConfigurator
    ),
    checker = KotlinOhosSourceSetLayoutChecker(
        MultiplatformOhosLayoutV2AgpRequirementChecker,
        MultiplatformOhosLayoutV2AndroidStyleSourceDirUsageChecker,
    )
)
