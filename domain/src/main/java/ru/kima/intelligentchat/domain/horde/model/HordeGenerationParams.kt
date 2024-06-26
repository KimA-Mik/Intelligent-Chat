package ru.kima.intelligentchat.domain.horde.model

data class HordeGenerationParams(
    val canAbort: Boolean = false,
    val frmtadsnsp: Boolean = false,
    val frmtrmblln: Boolean = false,
    val frmtrmspch: Boolean = false,
    val frmttriminc: Boolean = false,
    val grammar: String = String(),
    val guiSettings: Boolean = false,
    val maxContextLength: Int,
    val maxLength: Int,
    val minP: Float,
    val mirostat: Int,
    val mirostatEta: Float,
    val mirostatTau: Float,
    val n: Int = 1,
    val repPen: Float,
    val repetitionPenaltyRange: Int,
    val repetitionPenaltySlope: Float,
    val samplerOrder: List<Int>,
    val singleLine: Boolean = false,
    val stopSequence: List<String>,
    val streaming: Boolean = false,
    val temperature: Float,
    val tailFreeSampling: Float,
    val topA: Float,
    val topK: Int,
    val topP: Float,
    val typical: Float,
    val useDefaultBadWordsIds: Boolean,
    val useWorldInfo: Boolean
)