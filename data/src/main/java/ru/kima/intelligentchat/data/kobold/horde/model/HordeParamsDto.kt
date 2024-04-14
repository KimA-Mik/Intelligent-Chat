package ru.kima.intelligentchat.data.kobold.horde.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HordeParamsDto(
    @SerialName("can_abort") val canAbort: Boolean = false,
    //Input formatting option. When enabled, adds a leading space to your input if there is no trailing whitespace at the end of the previous action.
    @SerialName("frmtadsnsp") val frmtadsnsp: Boolean = false,
    //Output formatting option. When enabled, replaces all occurrences of two or more consecutive newlines in the output with one newline.
    @SerialName("frmtrmblln") val frmtrmblln: Boolean = false,
    //Output formatting option. When enabled, removes #/@%}{+=~|^<> from the output.
    @SerialName("frmtrmspch") val frmtrmspch: Boolean = false,
    //Output formatting option. When enabled, removes some characters from the end of the output such that the output doesn't end in the middle of a sentence. If the output is less than one sentence long, does nothing.
    @SerialName("frmttriminc") val frmttriminc: Boolean = false,
    @SerialName("grammar") val grammar: String = String(),
    @SerialName("gui_settings") val guiSettings: Boolean = false,
    @SerialName("max_context_length") val maxContextLength: Int,
    @SerialName("max_length") val maxLength: Int,
    @SerialName("min_p") val minP: Float,
    @SerialName("mirostat") val mirostat: Int,
    @SerialName("mirostat_eta") val mirostatEta: Float,
    @SerialName("mirostat_tau") val mirostatTau: Float,
    @SerialName("n") val n: Int = 1,
    @SerialName("rep_pen") val repPen: Float,
    @SerialName("rep_pen_range") val repetitionPenaltyRange: Int,
    @SerialName("rep_pen_slope") val repetitionPenaltySlope: Float,
    @SerialName("sampler_order") val samplerOrder: List<Int>,
    @SerialName("singleline") val singleLine: Boolean = false,
    @SerialName("stop_sequence") val stopSequence: List<String>,
    @SerialName("streaming") val streaming: Boolean = false,
    @SerialName("temperature") val temperature: Float,
    @SerialName("tfs") val tailFreeSampling: Float,
    @SerialName("top_a") val topA: Float,
    @SerialName("top_k") val topK: Int,
    @SerialName("top_p") val topP: Float,
    @SerialName("typical") val typical: Float,
    //When True, uses the default KoboldAI bad word IDs.
    @SerialName("use_default_badwordsids") val useDefaultBadWordsIds: Boolean,
    @SerialName("use_world_info") val useWorldInfo: Boolean
)