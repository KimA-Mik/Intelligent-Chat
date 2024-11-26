package ru.kima.intelligentchat.domain.preferences.horde.model

data class HordeState(
    val apiToken: String,
    val contextToWorker: Boolean,
    val responseToWorker: Boolean,
    val trustedWorkers: Boolean,
    val userName: String,
    val userId: Int,
    val userContextSize: Int,
    val actualContextSize: Int,
    val userResponseLength: Int,
    val actualResponseLength: Int,
    val modelsInfo: Map<String, HordeModelInfo>,
    val selectedModels: List<String>,
    val selectedPreset: Long,
    val generationId: String?,
)
