package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent
import ru.kima.intelligentchat.presentation.navigation.graphs.CARD_ID_ARGUMENT

class ChatScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val appPreferences: GetPreferencesUseCase
) : ViewModel() {

//    private val _state = MutableStateFlow(ChatScreenState())
//    val state = _state.asStateFlow()

    private val messages = MutableStateFlow(List(100) { it.toLong() })

    val state = combine(
        appPreferences(),
        savedStateHandle.getStateFlow(MESSAGE_INPUT_BUFFER, String()),
        messages
    ) { appPreferences, inputMessageBuffer, messages ->
        ChatScreenState(
            selectedPersona = appPreferences.selectedPersonaId,
            inputMessageBuffer = inputMessageBuffer,
            messages = messages
        )
    }

    init {
        val id = savedStateHandle.get<Long>(CARD_ID_ARGUMENT)
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