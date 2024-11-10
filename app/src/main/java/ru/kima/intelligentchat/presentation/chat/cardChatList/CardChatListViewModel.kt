package ru.kima.intelligentchat.presentation.chat.cardChatList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.presentation.chat.cardChatList.events.UiEvent
import ru.kima.intelligentchat.presentation.chat.cardChatList.events.UserEvent
import ru.kima.intelligentchat.presentation.chat.chatScreen.mappers.toDisplayCard
import ru.kima.intelligentchat.presentation.navigation.graphs.CARD_ID_ARGUMENT

class CardChatListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCharacterCard: GetCardUseCase,
) : ViewModel() {
    private val _uiEvent = MutableStateFlow(Event<UiEvent>(null))
    val uiEvent = _uiEvent.asStateFlow()

    private val card = MutableStateFlow(CharacterCard.default())
    val state = card.map { characterCard ->
        CardChatListState(
            displayCard = characterCard.toDisplayCard()
        )
    }

    init {
        initialize()
    }

    private fun initialize() {
        val cardId = savedStateHandle.get<Long>(CARD_ID_ARGUMENT)
        if (cardId == null || cardId < 1) {
            _uiEvent.update { Event(UiEvent.IncorrectInitialization) }
            return
        }

        viewModelScope.launch {
            getCharacterCard(cardId).collect {
                card.value = it
            }
        }
    }

    fun onEvent(event: UserEvent) {

    }
}