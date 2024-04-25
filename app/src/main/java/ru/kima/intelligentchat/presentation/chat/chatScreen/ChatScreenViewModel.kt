package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent
import ru.kima.intelligentchat.presentation.navigation.graphs.CARD_ID_ARGUMENT

class ChatScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val appPreferences: GetPreferencesUseCase,
    private val getCharacterCard: GetCardUseCase
) : ViewModel() {

//    private val _state = MutableStateFlow(ChatScreenState())
//    val state = _state.asStateFlow()

    private val messages = MutableStateFlow(List(100) { it.toLong() })

    private val _state = MutableStateFlow<ChatScreenState>(ChatScreenState.ChatState())
    val state = _state.asStateFlow()

    init {
        initialize()
    }

    private fun initialize() {
        val id = savedStateHandle.get<Long>(CARD_ID_ARGUMENT)
        if (id == null || id == 0L) {
            _state.value = ChatScreenState.ErrorState
            return
        }


        combine(
            getCharacterCard(id),
            savedStateHandle.getStateFlow(MESSAGE_INPUT_BUFFER, String()),
            messages
        ) { characterCard, inputMessageBuffer, messages ->
            _state.value = ChatScreenState.ChatState(
                characterCard = characterCard,
                inputMessageBuffer = inputMessageBuffer,
                messages = messages
            )
        }.launchIn(viewModelScope)
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