/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.utils

import org.jetbrains.kotlin.util.Logger
import kotlin.system.exitProcess

object KotlinLogger : Logger {
    override fun log(message: String) = println(message)
    override fun warning(message: String) = println("w: $message")
    override fun error(message: String) = println("e: $message")

    @Deprecated(Logger.FATAL_DEPRECATION_MESSAGE, ReplaceWith(Logger.FATAL_REPLACEMENT))
    override fun fatal(message: String): Nothing {
        error(message)
        exitProcess(1) // WARNING: This would stop the JVM process!
    }
}