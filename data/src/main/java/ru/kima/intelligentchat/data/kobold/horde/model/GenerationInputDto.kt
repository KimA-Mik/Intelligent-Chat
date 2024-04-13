package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerationInputDto(
    @SerialName("models") val models: List<String>,
    @SerialName("params") val params: HordeParamsDto,
    @SerialName("prompt") val prompt: String,
    @SerialName("trusted_workers") val trustedWorkers: Boolean
)