plugins {
    id("org.jetbrains.kotlin.jvm")
    id("gradle-plugin-compiler-dependency-configuration")
}

dependencies {
    embedded(project(":kotlin-atomicfu-compiler-plugin")) { isTransitive = false }
}

publish()

runtimeJar(rewriteDefaultJarDepsToShadedCompiler())
sourcesJarWithSourcesFromEmbedded(
    project(":kotlin-atomicfu-compiler-plugin").tasks.named<Jar>("sourcesJar")
)
javadocJarWithJavadocFromEmbedded(
    project(":kotlin-atomicfu-compiler-plugin").tasks.named<Jar>("javadocJar")
)
