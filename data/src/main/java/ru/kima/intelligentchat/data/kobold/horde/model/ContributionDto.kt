package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContributionDto(
    @SerialName("megapixelsteps") val megapixelsteps: Int = 0,
    @SerialName("tokens") val tokens: Int = 0
)