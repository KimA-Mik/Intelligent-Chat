package ru.kima.intelligentchat.domain.presets.kobold.model

data class KoboldPreset(
    val id: Long = 0,
    val name: String = String(),
    val temperature: Float = 1f,
    val repetitionPenalty: Float = 1f,
    val repetitionPenaltyRange: Int = 2048,
    val topP: Float = 0.5f,
    val topA: Float = 0.5f,
    val topK: Int = 50,
    val typical: Float = 0.5f,
    val tailFreeSampling: Float = 0.5f,
    val repetitionPenaltySlope: Float = 1f,
    val samplerOrder: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6),
    val mirostat: Int = 0,
    val mirostatTau: Float = 5f,
    val mirostatEta: Float = 0.1f,
    val grammar: String = String()
)

