package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestValidationErrors(
    @SerialName("additionalProp1") val additionalProp1: String = String(),
    @SerialName("additionalProp2") val additionalProp2: String = String(),
    @SerialName("additionalProp3") val additionalProp3: String = String(),
    @SerialName("additionalProp4") val additionalProp4: String = String(),
    @SerialName("additionalProp5") val additionalProp5: String = String()
)