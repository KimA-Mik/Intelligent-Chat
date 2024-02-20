package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestError(
    @SerialName("message")
    val message: String,
    @SerialName("rc")
    val returnCode: String
)