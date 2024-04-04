package ru.kima.intelligentchat.presentation.connection.presets.horde.edit.events

sealed interface UserEvent {
    data class EditTitle(val title: String) : UserEvent
    data class EditTemperature(val temperature: Float) : UserEvent
    data class EditTopK(val topK: Int) : UserEvent
    data class EditTopP(val topP: Float) : UserEvent
    data class EditTypical(val typical: Float) : UserEvent
    data class EditMinP(val minP: Float) : UserEvent
    data class EditTopA(val topA: Float) : UserEvent
    data class EditTailFreeSampling(val tailFreeSampling: Float) : UserEvent
    data class EditRepetitionPenalty(val repetitionPenalty: Float) : UserEvent
    data class EditRepetitionPenaltyRange(val repetitionPenaltyRange: Int) : UserEvent
    data class EditRepetitionPenaltySlope(val repetitionPenaltySlope: Float) : UserEvent

}