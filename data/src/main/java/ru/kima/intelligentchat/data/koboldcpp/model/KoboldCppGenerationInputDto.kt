package ru.kima.intelligentchat.data.koboldcpp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KoboldCppGenerationInputDto(
    val prompt: String,
    @SerialName("max_context_length") val maxContextLength: Int,
    @SerialName("max_length") val maxLength: Int,
    @SerialName("stop_sequence") val stopSequence: List<String>,
    val temperature: Float,
    @SerialName("rep_pen") val repetitionPenalty: Float,
    @SerialName("rep_pen_range") val repetitionPenaltyRange: Int,
    @SerialName("rep_pen_slope") val repetitionPenaltySlope: Float,
    @SerialName("top_p") val topP: Float,
    @SerialName("top_a") val topA: Float,
    @SerialName("top_k") val topK: Int,
    val typical: Float,
    val tfs: Float,
    @SerialName("min_p") val minP: Float,
    val mirostat: Int,
    @SerialName("mirostat_tau") val mirostatTau: Float,
    @SerialName("mirostat_eta") val mirostatEta: Float,
    @SerialName("sampler_order") val samplerOrder: List<Int>,
    val grammar: String,
    @SerialName("use_default_badwordsids") val useDefaultBadWordsIds: Boolean = true
)
