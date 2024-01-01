package ru.kima.intelligentchat.presentation.personas.details.events

import ru.kima.intelligentchat.R

sealed interface UiEvent {
    data class ShowSnackbar(val message: SnackbarMessage) : UiEvent {
        enum class SnackbarMessage(val mesId: Int) {
            PERSONA_SAVED(R.string.persona_saved_message)
        }
    }
}