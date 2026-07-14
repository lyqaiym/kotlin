plugins {
    kotlin("jvm")
}

//`:analysis:analysis-api-fe10` is used in IntelliJ Kotlin Plugin, it should be added to `extra["projectsDependingOnStableStdlib"]`
//publishJarsForIde(listOf(":analysis:analysis-api-fe10"))
publishJarsForIde(emptyList())