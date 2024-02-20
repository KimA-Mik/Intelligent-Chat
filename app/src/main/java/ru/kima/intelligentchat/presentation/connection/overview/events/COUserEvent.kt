package ru.kima.intelligentchat.presentation.connection.overview.events

import ru.kima.intelligentchat.core.common.API_TYPE

sealed interface COUserEvent {
    data class UpdateSelectedApi(val apiType: API_TYPE) : COUserEvent
    data class UpdateApiToken(val token: String) : COUserEvent
    data object ToggleHordeTokenVisibility : COUserEvent
    data object ToggleContextToWorker : COUserEvent
    data object ToggleResponseToWorker : COUserEvent
    data object ToggleTrustedWorkers : COUserEvent
    data object SaveApiKey : COUserEvent
}