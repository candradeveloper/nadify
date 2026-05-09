/**
 * Nadify Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.nacdev.nadify.models

import com.nadify.innertube.models.YTItem
import com.nacdev.nadify.db.entities.LocalItem

data class SimilarRecommendation(
    val title: LocalItem,
    val items: List<YTItem>,
)
