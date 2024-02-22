package ru.kima.intelligentchat.presentation.connection.overview.events

sealed interface COUiEvent {
    data class ShowMessage(val message: String) : COUiEvent
    data class ShowSnackbar(val snackbar: COSnackbar) : COUiEvent

    sealed interface COSnackbar {
        data object NoUser : COSnackbar
        data object ErrorGetKudos : COSnackbar
        data class ShowKudos(val kudos: Double) : COSnackbar
    }
}