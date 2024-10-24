package ru.kima.intelligentchat.domain.horde.model

data class HordeRequestStatus(
    val done: Boolean = false,
    val faulted: Boolean = false,
    val finished: Int = 0,
    val generations: List<HordeGeneration> = emptyList(),
    val isPossible: Boolean = true,
    val kudos: Double = 0.0,
    val processing: Int = 0,
    val queuePosition: Int = 0,
    val restarted: Int = 0,
    val waitTime: Int = 0,
    val waiting: Int = 0
)

data class HordeGeneration(
    val metadata: List<HordeGenerationMetadata> = listOf(),
    val model: String = String(),
    val seed: Int = 0,
    val state: String = String(),
    val text: String = String(),
    val workerId: String = String(),
    val workerName: String = String()
)

data class HordeGenerationMetadata(
    val ref: String = String(),
    val type: String = String(),
    val value: String = String()
)
