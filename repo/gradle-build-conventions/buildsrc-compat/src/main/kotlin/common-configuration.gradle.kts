import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.internal.file.collections.DefaultConfigurableFileCollection
import org.jetbrains.kotlin.buildtools.api.ExperimentalBuildToolsApi
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.internal.config.MavenComparableVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper
import org.jetbrains.kotlin.gradle.plugin.kotlinToolingVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import kotlin.collections.contains

// Contains common configuration that should be applied to all projects
plugins {
    id("implicit-dependencies")
}

// Common Group and version
val kotlinVersion: String by rootProject.extra
group = "org.jetbrains.kotlin"
version = kotlinVersion

project.configureJvmDefaultToolchain()
project.addEmbeddedConfigurations()
//project.addImplicitDependenciesConfiguration()
project.configureJavaCompile()
//project.configureJavaBasePlugin()
project.configureKotlinCompilationOptions()
project.configureArtifacts()
project.configureTests()
project.checkNoApiDependenciesOnK1Modules()

// There are problems with common build dir:
//  - some tests (in particular js and binary-compatibility-validator depend on the fixed (default) location
//  - idea seems unable to exclude common buildDir from indexing
// therefore it is disabled by default
// buildDir = File(commonBuildDir, project.name)

/**
 * Validates that the project does not expose K1 frontend modules
 * (see `fe10CompilerModules` in `gradle/compilerModules.gradle.kts`) through the `api`
 * configuration. K1 frontend modules must only be depended on via `implementation`,
 * so that the legacy frontend never leaks onto consumers' compile classpaths.
 */
fun Project.checkNoApiDependenciesOnK1Modules() {
    // The IDE-plugin dependency bundles under `:prepare:ide-plugin-dependencies` intentionally
    // re-export compiler modules (including the K1 frontend) via `api`, so that the IntelliJ
    // Kotlin plugin gets them on its classpath. They are the sanctioned re-exporters and are
    // exempt from this invariant.
    if (path.startsWith(":prepare:ide-plugin-dependencies")) return

    afterEvaluate {
        val apiConfiguration = configurations.findByName("api") ?: return@afterEvaluate

        @Suppress("UNCHECKED_CAST")
        val fe10CompilerModules = rootProject.extra["fe10CompilerModules"] as Array<String>

        @Suppress("UNCHECKED_CAST")
        val descriptorModules = rootProject.extra["descriptorsCompilerModules"] as Array<String>

        val k1Modules = (fe10CompilerModules + descriptorModules).toSet()

        println("afterEvaluate:fe10CompilerModules=${fe10CompilerModules.joinToString()}")
        println("afterEvaluate:descriptorModules=${descriptorModules.joinToString()}")
        val violations = apiConfiguration.dependencies
            .filterIsInstance<ProjectDependency>()
            .map { it.path }
            .filter {
                println("afterEvaluate:it=${it},in=${it in k1Modules}")
                it in k1Modules }
            .sorted()

        if (violations.isNotEmpty()) {
            throw GradleException(
                "Project '$path' declares `api` dependencies on K1 frontend modules: " +
                        violations.joinToString(prefix = "[", postfix = "]") + ". " +
                        "K1 frontend modules must only be depended on with the `implementation` " +
                        "configuration (see `fe10CompilerModules` in gradle/compilerModules.gradle.kts)."
            )
        }
    }
}

fun Project.addEmbeddedConfigurations() {
    configurations.maybeCreate("embedded").apply {
        isCanBeConsumed = false
        isCanBeResolved = true
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
            attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
        }
    }

    configurations.maybeCreate("embeddedElements").apply {
        extendsFrom(configurations["embedded"])
        isCanBeConsumed = true
        isCanBeResolved = false
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, objects.named("embedded-java-runtime"))
        }
    }
}

fun Project.configureJavaCompile() {
    plugins.withType<JavaPlugin> {
        tasks.withType<JavaCompile>().configureEach {
            options.compilerArgs.add("-Xlint:deprecation")
            options.compilerArgs.add("-Xlint:unchecked")
            if (!kotlinBuildProperties.disableWerror) {
                options.compilerArgs.add("-Werror")
            }
        }
    }
}

fun Project.configureJavaBasePlugin() {
    plugins.withId("java-base") {
        fun File.toProjectRootRelativePathOrSelf() = (relativeToOrNull(rootDir)?.takeUnless { it.startsWith("..") } ?: this).path

        fun FileCollection.printClassPath(role: String) =
            println("${project.path} $role classpath:\n  ${joinToString("\n  ") { it.toProjectRootRelativePathOrSelf() }}")

        val javaExtension = javaPluginExtension()
        tasks {
            register("printCompileClasspath") { doFirst { javaExtension.sourceSets["main"].compileClasspath.printClassPath("compile") } }
            register("printRuntimeClasspath") { doFirst { javaExtension.sourceSets["main"].runtimeClasspath.printClassPath("runtime") } }
            register("printTestCompileClasspath") { doFirst { javaExtension.sourceSets["test"].compileClasspath.printClassPath("test compile") } }
            register("printTestRuntimeClasspath") { doFirst { javaExtension.sourceSets["test"].runtimeClasspath.printClassPath("test runtime") } }
        }
    }
}

val projectsDependingOnStableStdlib: Array<String> by rootProject.extra
val kotlinApiVersionForProjectsDependingOnStableStdlib: String by rootProject.extra

/**
 * In all specified modules `-XXexplicit-return-types` flag will be added to warn about
 *   not specified return types for public declarations
 */
@Suppress("UNCHECKED_CAST")
val modulesWithRequiredExplicitTypes = rootProject.extra["firAllCompilerModules"] as Array<String>

fun Project.configureKotlinCompilationOptions() {
    plugins.withType<KotlinBasePluginWrapper> {
//        val commonCompilerArgs = listOfNotNull(
//            "-opt-in=kotlin.RequiresOptIn",
//            "-progressive".takeIf { getBooleanProperty("test.progressive.mode") ?: false },
//            "-Xdont-warn-on-error-suppression",
//            "-Xmulti-dollar-interpolation", // KT-2425
//            "-Xwhen-guards", // KT-13626
//            "-Xnon-local-break-continue", // KT-1436
//        )

        val kotlinLanguageVersion: String by rootProject.extra
        val useJvmFir by extra(project.kotlinBuildProperties.useFir)
        val useFirLT by extra(project.kotlinBuildProperties.useFirWithLightTree)
        val useFirIC by extra(project.kotlinBuildProperties.useFirTightIC)
        val renderDiagnosticNames by extra(project.kotlinBuildProperties.renderDiagnosticNames)

        tasks.withType<KotlinCompilationTask<*>>().configureEach {
            compilerOptions {
                val skipNewLanguageFeatures = skipArgumentForOlderKotlinCompilerVersion()

                val commonCompilerArgs = provider {
                    listOfNotNull(
                        "-opt-in=kotlin.RequiresOptIn",
                        "-progressive".takeIf { getBooleanProperty("test.progressive.mode") ?: false },
                        "-Xdont-warn-on-error-suppression",
                        "-Xcontext-parameters", // KT-72222
                        "-Xexplicit-backing-fields".takeUnless { skipNewLanguageFeatures }, // KT-14663
                        "-Xname-based-destructuring=complete".takeUnless { skipNewLanguageFeatures },
                        // Between making a language feature stable and the next bootstrap, we need to keep providing the compiler argument.
                        // But this produces a warning
                        // "The argument ... is redundant for the current language version ..."
                        // in the bootstrap test and fails because of -Werror.
                        // To work around it, we suppress the warning.
                        @OptIn(ExperimentalBuildToolsApi::class, ExperimentalKotlinGradlePluginApi::class)
                        "-Xwarning-level=REDUNDANT_CLI_ARG:disabled".takeIf {
                            project.kotlinExtension.compilerVersion.get() == project.kotlinToolingVersion.toString()
                        },
                    )
                }
                freeCompilerArgs.addAll(commonCompilerArgs)
                languageVersion.set(KotlinVersion.fromVersion(kotlinLanguageVersion))
                apiVersion.set(KotlinVersion.fromVersion(kotlinLanguageVersion))
                freeCompilerArgs.add("-Xskip-prerelease-check")

                if (project.path in projectsDependingOnStableStdlib) {
                    apiVersion.set(KotlinVersion.fromVersion(kotlinApiVersionForProjectsDependingOnStableStdlib))
                }
                if (project.path in modulesWithRequiredExplicitTypes) {
                    freeCompilerArgs.add("-XXexplicit-return-types=warning")
                }
            }

            val layout = project.layout
            val rootDir = rootDir
            val useAbsolutePathsInKlib = kotlinBuildProperties.getBoolean("kotlin.build.use.absolute.paths.in.klib")

            // Workaround to avoid remote build cache misses due to absolute paths in relativePathBaseArg
            // This is a workaround for KT-50876, but with no clear explanation why doFirst is used.
            // However, KGP with Native targets is used in the native-xctest project, and this code fails with
            //  The value for property 'freeCompilerArgs' is final and cannot be changed any further.
            if (project.path != ":native:kotlin-test-native-xctest" &&
                !project.path.startsWith(":native:objcexport-header-generator") &&
                !project.path.startsWith(":native:analysis-api-klib-reader") &&
                !project.path.startsWith(":native:external-projects-test-utils")
            ) {
                doFirst {
                    if (!useAbsolutePathsInKlib && this !is KotlinJvmCompile && this !is KotlinCompileCommon) {
                        @Suppress("DEPRECATION_ERROR", "DEPRECATION")
                        (this as KotlinCompile<*>).kotlinOptions.freeCompilerArgs +=
                            "-Xklib-relative-path-base=${layout.buildDirectory.get().asFile},${layout.projectDirectory.asFile},$rootDir"
                    }
                }
            }
        }

        val projectsWithOptInToUnsafeCastFunctionsFromAddToStdLib: List<String> by rootProject.extra

        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                if (renderDiagnosticNames) {
                    freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
                }
                allWarningsAsErrors.set(!kotlinBuildProperties.disableWerror)
                if (project.path in projectsWithOptInToUnsafeCastFunctionsFromAddToStdLib) {
                    freeCompilerArgs.add("-opt-in=org.jetbrains.kotlin.utils.addToStdlib.UnsafeCastFunction")
                }

//                if (!skipJvmDefaultAllForModule(project.path)) {
//                    freeCompilerArgs.add("-Xjvm-default=all")
//                }
//                -Xjvm-default is deprecated. Use -jvm-default instead.
                if (!skipJvmDefaultForModule(project.path)) {
                    freeCompilerArgs.add(
                        if (project.shouldUseOldJvmDefaultArgument())
                            "-Xjvm-default=all"
                        else
                            "-jvm-default=no-compatibility"
                    )
                } else {
                    freeCompilerArgs.add(
                        if (project.shouldUseOldJvmDefaultArgument())
                            "-Xjvm-default=disable"
                        else
                            "-jvm-default=disable"
                    )
                }
            }
        }
    }
}

private fun Project.shouldUseOldJvmDefaultArgument(): Boolean {
    @OptIn(ExperimentalBuildToolsApi::class, ExperimentalKotlinGradlePluginApi::class)
    val isOldCompilerVersion =
        MavenComparableVersion(kotlinExtension.compilerVersion.get()) < MavenComparableVersion("2.2")

    return isOldCompilerVersion
}

private val kotlinCompilerVersionForGradle = rootProject.extensions
    .getByType(VersionCatalogsExtension::class.java)
    .named("libs")
    .findVersion("kotlin-for-gradle-plugins-compilation")
    .get()
    .displayName

private fun Project.skipArgumentForOlderKotlinCompilerVersion(): Boolean {
    @OptIn(ExperimentalBuildToolsApi::class, ExperimentalKotlinGradlePluginApi::class)
    return MavenComparableVersion(kotlinExtension.compilerVersion.get()) <= MavenComparableVersion(kotlinCompilerVersionForGradle)
}

fun Project.configureArtifacts() {
    tasks.withType<Javadoc>().configureEach {
        enabled = false
    }

    tasks.withType<Jar>().configureEach {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    /**
     * Bit mask: `rw-r--r--`
     */
    fun ConfigurableFilePermissions.configureDefaultFilePermissions() {
        user {
            read = true
            write = true
            execute = false
        }
        group {
            read = true
            write = false
            execute = false
        }
        other {
            read = true
            write = false
            execute = false
        }
    }

    /**
     * Bit mask: `rwxr-xr-x`
     * Applies to both directories and executable files
     */
    fun ConfigurableFilePermissions.configureDefaultExecutableFilePermissions() {
        user {
            read = true
            write = true
            execute = true
        }
        group {
            read = true
            write = false
            execute = true
        }
        other {
            read = true
            write = false
            execute = true
        }
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
        filePermissions {
            configureDefaultFilePermissions()
        }
        dirPermissions {
            configureDefaultExecutableFilePermissions()
        }
        filesMatching("**/bin/*") {
            permissions {
                configureDefaultExecutableFilePermissions()
            }
        }
        filesMatching("**/bin/*.bat") {
            permissions {
                configureDefaultFilePermissions()
            }
        }
    }

    normalization {
        runtimeClasspath {
            ignore("META-INF/MANIFEST.MF")
            ignore("META-INF/compiler.version")
            ignore("META-INF/plugin.xml")
            ignore("kotlin/KotlinVersionCurrentValue.class")
        }
    }

    fun Task.listConfigurationContents(configName: String) {
        doFirst {
            project.configurations.findByName(configName)?.let {
                println("$configName configuration files:\n${it.allArtifacts.files.files.joinToString("\n  ", "  ")}")
            }
        }
    }

    tasks.register("listArchives") { listConfigurationContents("archives") }
    tasks.register("listDistJar") { listConfigurationContents("distJar") }
}

fun Project.configureTests() {
    val projectsUsingTcMutes = listOf(
        ":native",
        ":kotlin-native",
    )
    if (projectsUsingTcMutes.any { project.path.startsWith(it) }) {
        val ignoreTestFailures: Boolean by rootProject.extra
        tasks.configureEach {
            if (this is VerificationTask) {
                ignoreFailures = ignoreTestFailures
            }
        }
    }

    val concurrencyLimitService = project.gradle.sharedServices.registerIfAbsent(
        "concurrencyLimitService",
        ConcurrencyLimitService::class
    ) {
        maxParallelUsages = 1
    }

    tasks.withType<Test>().configureEach {
        if (!plugins.hasPlugin("compiler-tests-convention")) {
            outputs.doNotCacheIf("https://youtrack.jetbrains.com/issue/KTI-112") { true }
        }
        if (project.kotlinBuildProperties.limitTestTasksConcurrency) {
            usesService(concurrencyLimitService)
        }
    }

    // Aggregate task for build related checks
    tasks.register("checkBuild")
//    Could not determine the dependencies of task ':kotlin-gradle-plugin:check'.
//    > Could not create task ':kotlin-gradle-plugin:test'.
//    > Extension of type 'DevelocityTestConfiguration' does not exist. Currently registered extension types: [ExtraPropertiesExtension]
//    val mppProjects: List<String> by rootProject.extra
//    if (path !in mppProjects) {
//        configureTestRetriesForTestTasks()
//    }
}

// TODO: migrate remaining modules to the new JVM default scheme.
fun skipJvmDefaultForModule(path: String): Boolean =
// Gradle plugin modules are disabled because different Gradle versions bundle different Kotlin compilers,
    // and not all of them support the new JVM default scheme.
    "-gradle" in path || "-runtime" in path || path == ":kotlin-project-model" ||
            // Visitor/transformer interfaces in ir.tree are very sensitive to the way interface methods are implemented.
            // Enabling default method generation results in a performance loss of several % on full pipeline test on Kotlin.
            // TODO: investigate the performance difference and enable new mode for ir.tree.
            path == ":compiler:ir.tree" ||
            // Workaround a Proguard issue:
            //     java.lang.IllegalAccessError: tried to access method kotlin.reflect.jvm.internal.impl.types.checker.ClassicTypeSystemContext$substitutionSupertypePolicy$2.<init>(
            //       Lkotlin/reflect/jvm/internal/impl/types/checker/ClassicTypeSystemContext;Lkotlin/reflect/jvm/internal/impl/types/TypeSubstitutor;
            //     )V from class kotlin.reflect.jvm.internal.impl.resolve.OverridingUtilTypeSystemContext
            // KT-54749
            path == ":core:descriptors"


// Workaround for #KT-65266
afterEvaluate {
    val versionString = version.toString()
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        val realFriendPaths = (friendPaths as DefaultConfigurableFileCollection).shallowCopy()
        val friendPathsWithoutVersion = friendPaths.filter { !it.name.contains(versionString) }
        friendPaths.setFrom(friendPathsWithoutVersion)
        doFirst {
            friendPaths.setFrom(realFriendPaths)
        }
    }
}
