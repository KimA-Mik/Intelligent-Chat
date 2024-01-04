package ru.kima.intelligentchat.presentation.personas.details.events

import ru.kima.intelligentchat.R

sealed interface UiEvent {
    data class ShowSnackbar(val message: SnackbarMessage) : UiEvent {
        enum class SnackbarMessage(val mesId: Int) {
            PERSONA_SAVED(R.string.persona_saved_message),
            PERSONA_DELETED(R.string.persona_deleted_snackbar),
            PERSONA_NOT_DELETED(R.string.persona_not_deleted_snackbar)
        }
    }

    data object PopBack : UiEvent
}