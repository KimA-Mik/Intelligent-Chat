package ru.kima.intelligentchat.domain.preferences.horde.model

data class HordeModelInfo(
    val name: String,
    val count: Int = 0,
    val performance: Double = 0.0,
    val queued: Double = 0.0,
    val eta: Int = 0,
    val maxContextSize: Int = 0,
    val maxResponseLength: Int = 0
)
