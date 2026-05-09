package com.nadify.innertube.models.body

import com.nadify.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class SubscribeBody(
    val channelIds: List<String>,
    val context: Context,
    val params: String? = null,
)
