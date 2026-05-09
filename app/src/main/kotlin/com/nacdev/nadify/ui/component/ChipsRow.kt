/**
 * Nadify — Music Player by nacdev
 * Copyright (C) 2026 nacdev
 *
 * Filter chips Spotify-style:
 * - Pill shape, tidak ada border
 * - Aktif = ungu solid, teks putih
 * - Tidak aktif = abu gelap/terang
 */

package com.nacdev.nadify.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nacdev.nadify.R
import com.nacdev.nadify.ui.screens.OptionStats
import com.nacdev.nadify.ui.theme.NadifyPurple

@Composable
fun <E> ChipsRow(
    chips: List<Pair<E, String>>,
    currentValue: E,
    onValueUpdate: (E) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
) {
    val isDark = isSystemInDarkTheme()
    // Warna chip tidak aktif: abu gelap (dark) / abu terang (light)
    val inactiveColor = if (isDark) Color(0xFF2A2A2A) else Color(0xFFE0E0E0)
    val inactiveTextColor = if (isDark) Color.White else Color.Black

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)),
    ) {
        Spacer(Modifier.width(16.dp))

        chips.forEach { (value, label) ->
            val isSelected = currentValue == value
            FilterChip(
                label = {
                    Text(
                        text = label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                    )
                },
                selected = isSelected,
                colors = FilterChipDefaults.filterChipColors(
                    // Aktif = ungu Nadify
                    selectedContainerColor    = NadifyPurple,
                    selectedLabelColor        = Color.White,
                    // Tidak aktif = abu
                    containerColor            = inactiveColor,
                    labelColor                = inactiveTextColor,
                ),
                onClick = { onValueUpdate(value) },
                // Pill shape
                shape = RoundedCornerShape(50),
                border = null,
            )
            Spacer(Modifier.width(8.dp))
        }

        Spacer(Modifier.width(8.dp))
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun <Int> ChoiceChipsRow(
    chips: List<Pair<Int, String>>,
    options: List<Pair<OptionStats, String>>,
    selectedOption: OptionStats,
    onSelectionChange: (OptionStats) -> Unit,
    currentValue: Int,
    onValueUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
) {
    val isDark = isSystemInDarkTheme()
    val inactiveColor = if (isDark) Color(0xFF2A2A2A) else Color(0xFFE0E0E0)

    var expandIconDegree by remember { mutableFloatStateOf(0f) }
    val rotationAnimation by animateFloatAsState(
        targetValue = expandIconDegree,
        animationSpec = tween(durationMillis = 300),
        label = "",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)),
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column {
            AssistChip(
                onClick = {
                    expanded = !expanded
                    expandIconDegree -= 180
                },
                label = {
                    Text(
                        text = when (selectedOption) {
                            OptionStats.WEEKS    -> stringResource(R.string.weeks)
                            OptionStats.MONTHS   -> stringResource(R.string.months)
                            OptionStats.YEARS    -> stringResource(R.string.years)
                            OptionStats.CONTINUOUS -> stringResource(R.string.continuous)
                        },
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.expand_more),
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer(rotationZ = rotationAnimation),
                    )
                },
                shape = RoundedCornerShape(50),
                border = null,
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = inactiveColor,
                    labelColor     = MaterialTheme.colorScheme.onSurface,
                ),
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandIn() + fadeIn(),
                exit  = shrinkOut() + fadeOut(),
            ) {
                DropdownMenu(
                    modifier  = Modifier.padding(start = 12.dp),
                    expanded  = expanded,
                    onDismissRequest = {
                        expanded = false
                        expandIconDegree -= 180
                    },
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text    = { Text(option.second) },
                            onClick = {
                                onSelectionChange(option.first)
                                expandIconDegree -= 180
                                expanded = false
                            },
                        )
                    }
                }
            }
        }

        AnimatedContent(
            targetState = selectedOption,
            transitionSpec = {
                slideInHorizontally() + fadeIn() togetherWith slideOutHorizontally() + fadeOut()
            },
            label = "",
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)),
            ) {
                chips.forEach { (value, label) ->
                    Spacer(Modifier.width(8.dp))
                    val isSelected = currentValue == value
                    FilterChip(
                        label = {
                            Text(
                                label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                            )
                        },
                        selected = isSelected,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NadifyPurple,
                            selectedLabelColor     = Color.White,
                            containerColor         = inactiveColor,
                            labelColor             = MaterialTheme.colorScheme.onSurface,
                        ),
                        onClick = { onValueUpdate(value) },
                        shape  = RoundedCornerShape(50),
                        border = null,
                    )
                }
            }
        }
    }
}
