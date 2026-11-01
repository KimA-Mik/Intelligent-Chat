package ru.kima.intelligentchat.data.koboldcpp.model

import kotlinx.serialization.Serializable

@Serializable
data class KoboldCppGenerationResponseDto(
    val results: List<KoboldCppGenerationResultDto> = emptyList()
)

@Serializable
data class KoboldCppGenerationResultDto(
    val text: String
)
