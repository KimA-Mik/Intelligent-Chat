package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestValidationError(
    @SerialName("errors") val errors: RequestValidationErrors = RequestValidationErrors(),
    @SerialName("message") val message: String = String(),
    @SerialName("rc") val rc: String = String()
)