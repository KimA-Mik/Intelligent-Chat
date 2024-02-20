package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FulfillmentDto(
    @SerialName("image") val image: Int = 0,
    @SerialName("text") val text: Int = 0,
    @SerialName("interrogation") val interrogation: Int = 0
)