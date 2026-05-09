/**
 * Nadify Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.nacdev.nadify.extensions

fun <T> tryOrNull(block: () -> T): T? =
    try {
        block()
    } catch (e: Exception) {
        null
    }
