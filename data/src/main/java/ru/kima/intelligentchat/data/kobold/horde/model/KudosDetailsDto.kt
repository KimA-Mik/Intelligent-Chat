package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KudosDetailsDto(
    @SerialName("accumulated") val accumulated: Double = 0.0,
    @SerialName("gifted") val gifted: Double = 0.0,
    @SerialName("admin") val admin: Double = 0.0,
    @SerialName("received") val received: Double = 0.0,
    @SerialName("recurring") val recurring: Double = 0.0,
    @SerialName("awarded") val awarded: Double = 0.0
)