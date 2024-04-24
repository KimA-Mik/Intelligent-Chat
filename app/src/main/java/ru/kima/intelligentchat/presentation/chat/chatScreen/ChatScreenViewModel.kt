package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.combine
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent

class ChatScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val appPreferences: GetPreferencesUseCase
) : ViewModel() {

//    private val _state = MutableStateFlow(ChatScreenState())
//    val state = _state.asStateFlow()

    val state = combine(
        appPreferences(),
        savedStateHandle.getStateFlow(MESSAGE_INPUT_BUFFER, String())
    ) { appPreferences, inputMessageBuffer ->
        ChatScreenState(
            selectedPersona = appPreferences.selectedPersonaId,
            inputMessageBuffer = inputMessageBuffer
        )
    }


    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.UpdateInputMessage -> onUpdateInputMessage(event.message)
        }
    }

    private fun onUpdateInputMessage(message: String) {
        savedStateHandle[MESSAGE_INPUT_BUFFER] = message
    }

    companion object {
        private const val MESSAGE_INPUT_BUFFER = "MESSAGE_INPUT_BUFFER"
    }
}