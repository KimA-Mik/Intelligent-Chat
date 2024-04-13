package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestSingleWarningDto(
    @SerialName("code") val code: String = String(),
    @SerialName("message") val message: String = String()
)