package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsageDto(
    @SerialName("megapixelsteps") val megapixelsteps: Int? = 0,
    @SerialName("requests") val requests: Int? = 0
)