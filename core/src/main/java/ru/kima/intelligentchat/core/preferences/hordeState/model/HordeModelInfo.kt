package ru.kima.intelligentchat.core.preferences.hordeState.model

import kotlinx.serialization.Serializable

@Serializable
data class HordeModelInfo(
    val name: String,
    val count: Int = 0,
    val performance: Double = 0.0,
    val queued: Double = 0.0,
    val eta: Int = 0,
    val maxContextSize: Int = 0,
    val maxResponseLength: Int = 0
)
