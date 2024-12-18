package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContributionsDto(
    @SerialName("megapixelsteps") val megapixelsteps: Double? = 0.0,
    @SerialName("fulfillments") val fulfillments: Int? = 0
)