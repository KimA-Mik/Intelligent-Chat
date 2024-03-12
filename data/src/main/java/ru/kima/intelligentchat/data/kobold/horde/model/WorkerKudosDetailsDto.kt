package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkerKudosDetailsDto(
    @SerialName("generated") val generated: Int,
    @SerialName("uptime") val uptime: Int
)