package ru.kima.intelligentchat.domain.horde.model

data class ActiveModel(
    val name: String = String(),
    val count: Int = 0,
    val performance: Double = 0.0,
    val queued: Double = 0.0,
    val jobs: Double = 0.0,
    val eta: Int = 0,
    val type: String = String()
)