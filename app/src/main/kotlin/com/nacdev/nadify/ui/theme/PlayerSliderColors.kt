/**
 * Nadify — Music Player by nacdev
 * Copyright (C) 2026 nacdev
 *
 * Slider warna ungu Nadify, track inaktif abu
 */

package com.nacdev.nadify.ui.theme

import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nacdev.nadify.constants.PlayerBackgroundStyle

object PlayerSliderColors {

    @Composable
    fun getSliderColors(
        activeColor: Color,
        playerBackground: PlayerBackgroundStyle,
        useDarkTheme: Boolean,
    ): SliderColors {
        val nadifyActive = NadifyPurple

        val inactiveTrackColor = when (playerBackground) {
            PlayerBackgroundStyle.DEFAULT ->
                if (useDarkTheme) Color(0xFF444444) else Color(0xFFCAC4D0)
            PlayerBackgroundStyle.BLUR, PlayerBackgroundStyle.GRADIENT ->
                Color.White.copy(alpha = 0.3f)
        }

        return SliderDefaults.colors(
            activeTrackColor           = nadifyActive,
            activeTickColor            = nadifyActive,
            thumbColor                 = nadifyActive,
            inactiveTrackColor         = inactiveTrackColor,
            disabledActiveTrackColor   = nadifyActive.copy(alpha = 0.5f),
            disabledInactiveTrackColor = inactiveTrackColor,
            disabledThumbColor         = nadifyActive.copy(alpha = 0.5f),
        )
    }
}
