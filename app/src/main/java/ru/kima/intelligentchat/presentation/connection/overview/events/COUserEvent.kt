package ru.kima.intelligentchat.presentation.connection.overview.events

import ru.kima.intelligentchat.domain.common.ApiType

sealed interface COUserEvent {
    data class UpdateSelectedApi(val apiType: ApiType) : COUserEvent
    data class UpdateApiToken(val token: String) : COUserEvent
    data object ToggleHordeTokenVisibility : COUserEvent
    data object ToggleContextToWorker : COUserEvent
    data object ToggleResponseToWorker : COUserEvent
    data object ToggleTrustedWorkers : COUserEvent
    data object SaveApiKey : COUserEvent
    data object ShowKudos : COUserEvent
    data object RefreshModels : COUserEvent
    data object OpenSelectHordeModelsDialog : COUserEvent
    data object DismissSelectHordeModelsDialog : COUserEvent
    data object AcceptSelectHordeModelsDialog : COUserEvent
    data class CheckHordeModel(val model: String) : COUserEvent
    data class UpdateHordeContextSize(val newSize: Int) : COUserEvent
    data class UpdateHordeResponseLength(val newLength: Int) : COUserEvent
    data class SelectHordePreset(val presetId: Long) : COUserEvent
    data object EditPreset : COUserEvent
}