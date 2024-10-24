package ru.kima.intelligentchat.domain.horde.model

data class HordeAsyncRequest(
    val id: String = String(),
    val kudos: Double = 0.0,
    val message: String = String(),
    val warnings: List<String> = emptyList()
)
