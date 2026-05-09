/**
 * Nadify — Music Player by nacdev
 * Copyright (C) 2026 nacdev
 *
 * Bottom navigation Nadify-style:
 * - Background hitam solid (dark) / putih solid (light)
 * - Icon aktif = putih/hitam, tidak aktif = abu
 * - Dot ungu kecil di bawah icon aktif (brand Nadify)
 */

package com.nacdev.nadify.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nacdev.nadify.ui.screens.Screens
import com.nacdev.nadify.ui.theme.NadifyPurple
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

private fun isRouteSelected(
    currentRoute: String?,
    screenRoute: String,
    navigationItems: List<Screens>,
): Boolean {
    if (currentRoute == null) return false
    if (currentRoute == screenRoute) return true
    if (navigationItems.any { it.route == screenRoute } &&
        currentRoute.startsWith("$screenRoute/")) return true
    if (screenRoute == "search_input" &&
        (currentRoute.startsWith("search/") || currentRoute == "search/{query}")) return true
    return false
}

@Composable
fun AppNavigationRail(
    navigationItems: List<Screens>,
    currentRoute: String?,
    onItemClick: (Screens, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    pureBlack: Boolean = false,
    onSearchLongClick: (() -> Unit)? = null,
) {
    val isDark = isSystemInDarkTheme()
    val containerColor = if (pureBlack || isDark) Color.Black else Color.White
    val haptics = LocalHapticFeedback.current
    val viewConfig = LocalViewConfiguration.current

    NavigationRail(modifier = modifier, containerColor = containerColor) {
        Spacer(Modifier.weight(1f))
        navigationItems.forEach { screen ->
            val isSelected = remember(currentRoute, screen.route) {
                isRouteSelected(currentRoute, screen.route, navigationItems)
            }
            val currentIsSelected by rememberUpdatedState(isSelected)
            val iconRes = if (isSelected) screen.iconIdActive else screen.iconIdInactive
            val isSearch = screen == Screens.Search && onSearchLongClick != null
            val source = remember { MutableInteractionSource() }

            if (isSearch) {
                LaunchedEffect(source) {
                    var longClick = false
                    source.interactions.collectLatest { interaction ->
                        when (interaction) {
                            is PressInteraction.Press -> {
                                longClick = false
                                delay(viewConfig.longPressTimeoutMillis)
                                longClick = true
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                onSearchLongClick?.invoke()
                            }
                            is PressInteraction.Release -> if (!longClick) onItemClick(screen, currentIsSelected)
                            is PressInteraction.Cancel  -> longClick = false
                        }
                    }
                }
            }

            NavigationRailItem(
                selected          = isSelected,
                onClick           = { if (!isSearch) onItemClick(screen, currentIsSelected) },
                interactionSource = source,
                colors            = NavigationRailItemDefaults.colors(
                    selectedIconColor   = if (isDark) Color.White else Color.Black,
                    unselectedIconColor = Color(0xFF777777),
                    indicatorColor      = Color.Transparent,
                ),
                icon = {
                    Icon(painterResource(iconRes), stringResource(screen.titleId))
                },
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun AppNavigationBar(
    navigationItems: List<Screens>,
    currentRoute: String?,
    onItemClick: (Screens, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    pureBlack: Boolean = false,
    slimNav: Boolean = false,
    onSearchLongClick: (() -> Unit)? = null,
) {
    val isDark = isSystemInDarkTheme()
    val containerColor = if (pureBlack || isDark) Color.Black else Color.White
    val selectedColor   = if (isDark) Color.White else Color.Black
    val unselectedColor = Color(0xFF777777)
    val haptics = LocalHapticFeedback.current
    val viewConfig = LocalViewConfiguration.current

    NavigationBar(
        modifier       = modifier,
        containerColor = containerColor,
        tonalElevation = 0.dp,
    ) {
        navigationItems.forEach { screen ->
            val isSelected = remember(currentRoute, screen.route) {
                isRouteSelected(currentRoute, screen.route, navigationItems)
            }
            val currentIsSelected by rememberUpdatedState(isSelected)
            val iconRes = if (isSelected) screen.iconIdActive else screen.iconIdInactive
            val isSearch = screen == Screens.Search && onSearchLongClick != null
            val source = remember { MutableInteractionSource() }

            if (isSearch) {
                LaunchedEffect(source) {
                    var longClick = false
                    source.interactions.collectLatest { interaction ->
                        when (interaction) {
                            is PressInteraction.Press -> {
                                longClick = false
                                delay(viewConfig.longPressTimeoutMillis)
                                longClick = true
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                onSearchLongClick?.invoke()
                            }
                            is PressInteraction.Release -> if (!longClick) onItemClick(screen, currentIsSelected)
                            is PressInteraction.Cancel  -> longClick = false
                        }
                    }
                }
            }

            NavigationBarItem(
                selected          = isSelected,
                onClick           = { if (!isSearch) onItemClick(screen, currentIsSelected) },
                interactionSource = source,
                colors            = NavigationBarItemDefaults.colors(
                    selectedIconColor   = selectedColor,
                    selectedTextColor   = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor      = Color.Transparent,
                ),
                icon = {
                    Box {
                        Icon(painterResource(iconRes), stringResource(screen.titleId))
                        // Dot ungu Nadify di bawah icon aktif
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(NadifyPurple, CircleShape)
                                    .align(Alignment.BottomCenter),
                            )
                        }
                    }
                },
                label = if (!slimNav) {
                    {
                        Text(
                            text       = stringResource(screen.titleId),
                            maxLines   = 1,
                            overflow   = TextOverflow.Ellipsis,
                            fontSize   = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        )
                    }
                } else null,
            )
        }
    }
}
