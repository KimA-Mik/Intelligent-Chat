package ru.kima.intelligentchat.domain.horde.model

data class GenerationInput(
    val models: List<String>,
    val params: HordeGenerationParams,
    val prompt: String,
    val trustedWorkers: Boolean
)
