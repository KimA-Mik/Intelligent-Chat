package ru.kima.intelligentchat.presentation.charactersList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.AddCardFromPngUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardsUseCase
import ru.kima.intelligentchat.domain.card.useCase.PutCardUseCase
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUiEvent
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUserEvent

class CharactersListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCards: GetCardsUseCase,
    private val putCard: PutCardUseCase,
    private val putCardFromImage: AddCardFromPngUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CharactersListState())
    val state = _state.asStateFlow()

    private val _uiEvents = MutableSharedFlow<CharactersListUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        loadCards()
    }

    fun loadCards() = viewModelScope.launch {
        getCards().collect { result ->
            if (result is Resource.Success) {
                _state.update {
                    it.copy(
                        cards = result.data!!
                    )
                }
            }
        }
    }

    fun onUserEvent(event: CharactersListUserEvent) {
        when (event) {
            is CharactersListUserEvent.CardSelected -> onCardSelected(event.cardId)
            is CharactersListUserEvent.AddCardFromImage -> addCardFromPng(event.imageBytes)
            CharactersListUserEvent.AddCardFromImageClicked -> onAddCardFromImageClicked()
            CharactersListUserEvent.CreateCardClicked -> createEmptyCardClicked()
        }
    }

    private fun onCardSelected(cardId: Long) {
        viewModelScope.launch { _uiEvents.emit(CharactersListUiEvent.NavigateTo(cardId)) }
    }

    private fun addCardFromPng(png: ByteArray) {
        putCardFromImage(png).onEach { resource ->
            when (resource) {
                is Resource.Error -> {
                    _uiEvents.emit(CharactersListUiEvent.SnackbarMessage(resource.message!!))
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    _uiEvents.emit(CharactersListUiEvent.NavigateTo(resource.data!!))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun onAddCardFromImageClicked() {
        viewModelScope.launch {
            _uiEvents.emit(CharactersListUiEvent.SelectPngImage)
        }
    }

    private fun createEmptyCardClicked() {
        viewModelScope.launch {
            val cardId = putCard(CharacterCard())
            _uiEvents.emit(CharactersListUiEvent.NavigateTo(cardId))
        }
    }
}