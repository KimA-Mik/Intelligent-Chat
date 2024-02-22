package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActiveModelDto(
    @SerialName("name") val name: String = "",
    @SerialName("count") val count: Int = 0,
    @SerialName("performance") val performance: Double = 0.0,
    @SerialName("queued") val queued: Double = 0.0,
    @SerialName("jobs") val jobs: Double = 0.0,
    @SerialName("eta") val eta: Int = 0,
    @SerialName("type") val type: String = ""
)