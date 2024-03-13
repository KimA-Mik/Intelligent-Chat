package ru.kima.intelligentchat.domain.horde.model

data class HordeWorker(
    val id: String,
    val maintenanceMode: Boolean,
    val maxContextLength: Int,
    val maxLength: Int,
    val models: List<String>,
    val name: String,
    val nsfw: Boolean,
    val online: Boolean,
    val trusted: Boolean,
)