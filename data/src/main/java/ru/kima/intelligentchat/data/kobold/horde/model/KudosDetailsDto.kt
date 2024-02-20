package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KudosDetailsDto(
    @SerialName("accumulated") val accumulated: Int = 0,
    @SerialName("gifted") val gifted: Int = 0,
    @SerialName("admin") val admin: Int = 0,
    @SerialName("received") val received: Int = 0,
    @SerialName("recurring") val recurring: Int = 0,
    @SerialName("awarded") val awarded: Int = 0
)