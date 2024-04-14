package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAsyncDto(
    @SerialName("id") val id: String = String(),
    @SerialName("kudos") val kudos: Int = 0,
    @SerialName("message") val message: String = String(),
    @SerialName("warnings") val warnings: List<RequestSingleWarningDto> = emptyList()
)