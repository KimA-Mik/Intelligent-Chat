package ru.kima.intelligentchat.presentation.connection.overview.events

sealed interface COUiEvent {
    data class ShowMessage(val message: String) : COUiEvent
}