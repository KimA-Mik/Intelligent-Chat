package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.useCase.CreateAndSelectChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToCardChatUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent
import ru.kima.intelligentchat.presentation.chat.chatScreen.mappers.toDisplayCard
import ru.kima.intelligentchat.presentation.chat.chatScreen.mappers.toDisplayChat
import ru.kima.intelligentchat.presentation.navigation.graphs.CARD_ID_ARGUMENT

class ChatScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val appPreferences: GetPreferencesUseCase,
    private val getCharacterCard: GetCardUseCase,
    private val createAndSelectChat: CreateAndSelectChatUseCase,
    private val subscribeToCardChat: SubscribeToCardChatUseCase
) : ViewModel() {
    private val characterCard = MutableStateFlow(CharacterCard())

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

        viewModelScope.launch {
            getCharacterCard(id).collect {
                if (it.selectedChat == 0L) {
                    createAndSelectChat(it)
                }
                characterCard.value = it
            }
        }


        val chat = subscribeToCardChat(id)
            .map {
                when (it) {
                    SubscribeToCardChatUseCase.Result.NotFound -> FullChat()
                    is SubscribeToCardChatUseCase.Result.Success -> it.fullChat
                    SubscribeToCardChatUseCase.Result.UnknownError -> FullChat()
                }
            }

        val chatInfo = combine(
            characterCard, chat
        ) { characterCard, fullChat ->
            ChatScreenState.ChatState.ChatInfo(
                characterCard = characterCard.toDisplayCard(),
                fullChat = fullChat.toDisplayChat(characterCard)
            )
        }

        combine(
            chatInfo,
            savedStateHandle.getStateFlow(MESSAGE_INPUT_BUFFER, String()),
        ) { info, inputMessageBuffer ->
            ChatScreenState.ChatState(
                info = info,
                inputMessageBuffer = inputMessageBuffer,
            )
        }.onEach {
            _state.value = it
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