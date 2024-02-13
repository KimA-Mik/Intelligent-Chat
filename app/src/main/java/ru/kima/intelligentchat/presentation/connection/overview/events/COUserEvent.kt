package ru.kima.intelligentchat.presentation.connection.overview.events

import ru.kima.intelligentchat.core.common.API_TYPE

sealed interface COUserEvent {
    data class UpdateSelectedApi(val apiType: API_TYPE) : COUserEvent
    data class UpdateApiToken(val token: String) : COUserEvent
    data object ToggleHordeTokenVisibility : COUserEvent
}