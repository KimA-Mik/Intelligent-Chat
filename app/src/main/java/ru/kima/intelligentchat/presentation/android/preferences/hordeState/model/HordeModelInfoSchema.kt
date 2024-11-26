package ru.kima.intelligentchat.presentation.android.preferences.hordeState.model

import kotlinx.serialization.Serializable

@Serializable
data class HordeModelInfoSchema(
    val name: String,
    val count: Int = 0,
    val performance: Double = 0.0,
    val queued: Double = 0.0,
    val eta: Int = 0,
    val maxContextSize: Int = 0,
    val maxResponseLength: Int = 0
)
