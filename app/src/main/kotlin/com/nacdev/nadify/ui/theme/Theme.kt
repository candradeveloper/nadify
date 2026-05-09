/**
 * Nadify — Music Player by nacdev
 * Copyright (C) 2026 nacdev
 *
 * Tema eksklusif Nadify:
 * Warna utama Ungu #7C4DFF, dark #0E0E0E, light #FFFFFF
 * Dibuat dari nol — bukan salinan proyek lain
 */

package com.nacdev.nadify.ui.theme

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme
import com.materialkolor.score.Score

// ── Warna brand Nadify ───────────────────────────────────────────────────────
val NadifyPurple       = Color(0xFF7C4DFF)   // Ungu utama
val NadifyPurpleLight  = Color(0xFF9E7AFF)   // Ungu terang (hover/active)
val NadifyPurpleDark   = Color(0xFF4A00E8)   // Ungu gelap
val NadifyBlack        = Color(0xFF000000)
val NadifyDark         = Color(0xFF0E0E0E)   // Background dark (lebih gelap dari Spotify)
val NadifyDark2        = Color(0xFF161616)
val NadifyDark3        = Color(0xFF242424)
val NadifyGray         = Color(0xFFAAAAAA)

val DefaultThemeColor  = NadifyPurple

// ── Dark scheme — Nadify dark mode ──────────────────────────────────────────
private val NadifyDarkColorScheme = darkColorScheme(
    primary              = NadifyPurple,
    onPrimary            = Color.White,
    primaryContainer     = Color(0xFF3A1A99),
    onPrimaryContainer   = NadifyPurpleLight,
    secondary            = NadifyGray,
    onSecondary          = Color.Black,
    secondaryContainer   = NadifyDark3,
    onSecondaryContainer = Color.White,
    tertiary             = NadifyPurpleLight,
    background           = NadifyDark,
    onBackground         = Color.White,
    surface              = NadifyDark,
    onSurface            = Color.White,
    surfaceVariant       = NadifyDark2,
    onSurfaceVariant     = NadifyGray,
    surfaceContainer     = NadifyDark2,
    surfaceContainerHigh = NadifyDark3,
    outline              = Color(0xFF444444),
    error                = Color(0xFFCF6679),
    onError              = Color.Black,
)

// ── Light scheme — Nadify light mode ────────────────────────────────────────
private val NadifyLightColorScheme = lightColorScheme(
    primary              = NadifyPurple,
    onPrimary            = Color.White,
    primaryContainer     = Color(0xFFE8DEFF),
    onPrimaryContainer   = Color(0xFF21005D),
    secondary            = Color(0xFF625B71),
    onSecondary          = Color.White,
    secondaryContainer   = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary             = NadifyPurpleDark,
    background           = Color.White,
    onBackground         = Color.Black,
    surface              = Color.White,
    onSurface            = Color.Black,
    surfaceVariant       = Color(0xFFF4F0FF),
    onSurfaceVariant     = Color(0xFF49454F),
    surfaceContainer     = Color(0xFFF4F0FF),
    surfaceContainerHigh = Color(0xFFEAE0F8),
    outline              = Color(0xFFCAC4D0),
    error                = Color(0xFFB3261E),
    onError              = Color.White,
)

@Composable
fun NadifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    pureBlack: Boolean = false,
    themeColor: Color = DefaultThemeColor,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current

    val useSystemDynamic =
        themeColor == Color(0xFFED5564) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val colorScheme: ColorScheme? = remember(themeColor, darkTheme, pureBlack, useSystemDynamic) {
        when {
            useSystemDynamic -> {
                val d = if (darkTheme) dynamicDarkColorScheme(context)
                        else dynamicLightColorScheme(context)
                if (pureBlack && darkTheme) d.pureBlack(true) else d
            }
            themeColor == DefaultThemeColor -> {
                if (darkTheme) {
                    if (pureBlack) NadifyDarkColorScheme.pureBlack(true)
                    else NadifyDarkColorScheme
                } else NadifyLightColorScheme
            }
            else -> null
        }
    }

    val finalScheme = if (colorScheme != null) {
        colorScheme
    } else {
        val dynamic = rememberDynamicColorScheme(
            seedColor   = themeColor,
            isDark      = darkTheme,
            specVersion = ColorSpec.SpecVersion.SPEC_2025,
            style       = PaletteStyle.TonalSpot,
        )
        remember(dynamic, pureBlack, darkTheme) {
            if (pureBlack && darkTheme) dynamic.pureBlack(true) else dynamic
        }
    }

    MaterialTheme(
        colorScheme = finalScheme,
        typography  = NadifyTypography,
        content     = content,
    )
}

// ── Palette helpers ──────────────────────────────────────────────────────────
fun Bitmap.extractThemeColor(): Color {
    val map = Palette.from(this).maximumColorCount(8).generate()
        .swatches.associate { it.rgb to it.population }
    return Color(Score.score(map).first())
}

fun Bitmap.extractGradientColors(): List<Color> {
    val map = Palette.from(this).maximumColorCount(64).generate()
        .swatches.associate { it.rgb to it.population }
    val ranked = Score.score(map, 2, 0xff7c4dff.toInt(), true)
        .sortedByDescending { Color(it).luminance() }
    return if (ranked.size >= 2)
        listOf(Color(ranked[0]), Color(ranked[1]))
    else
        listOf(Color(0xFF3A1A99), Color(0xFF0E0E0E))
}

fun ColorScheme.pureBlack(apply: Boolean) =
    if (apply) copy(
        surface              = Color.Black,
        background           = Color.Black,
        surfaceContainer     = Color(0xFF080808),
        surfaceContainerHigh = Color(0xFF101010),
    ) else this

val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color = Color(value)
    override fun SaverScope.save(value: Color): Int = value.toArgb()
}
