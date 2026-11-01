package ru.kima.intelligentchat.domain.messaging.generation.model

data class KoboldCppGenerationInput(
    val prompt: String,
    val maxContextLength: Int,
    val maxLength: Int,
    val stopSequence: List<String>,
    val temperature: Float,
    val repetitionPenalty: Float,
    val repetitionPenaltyRange: Int,
    val repetitionPenaltySlope: Float,
    val topP: Float,
    val topA: Float,
    val topK: Int,
    val typical: Float,
    val tailFreeSampling: Float,
    val minP: Float,
    val mirostat: Int,
    val mirostatTau: Float,
    val mirostatEta: Float,
    val samplerOrder: List<Int>,
    val grammar: String
)
