package ru.kima.intelligentchat.presentation.charactersList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.AddCardFromPngUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardsUseCase
import ru.kima.intelligentchat.domain.card.useCase.PutCardUseCase
import ru.kima.intelligentchat.domain.card.util.getCardPhotoName
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUiEvent
import ru.kima.intelligentchat.presentation.charactersList.events.CharactersListUserEvent

class CharactersListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCards: GetCardsUseCase,
    private val putCard: PutCardUseCase,
    private val putCardFromImage: AddCardFromPngUseCase
) : ViewModel() {
    private val cards = MutableStateFlow(emptyList<CharacterCard>())
    private val query = savedStateHandle.getStateFlow("query", String())

    val state = combine(
        cards,
        query
    ) { cards, query ->
        CharactersListState(cards = cards, searchText = query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CharactersListState())

    private val _uiEvents = MutableSharedFlow<CharactersListUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        loadCards()
    }

    private fun loadCards() = viewModelScope.launch {
        getCards(query.value).collect { result ->
            cards.value = result
        }
    }

    fun onUserEvent(event: CharactersListUserEvent) {
        when (event) {
            is CharactersListUserEvent.CardSelected -> onCardSelected(event.cardId)
            is CharactersListUserEvent.AddCardFromImage -> addCardFromPng(event.imageBytes)
            CharactersListUserEvent.AddCardFromImageClicked -> onAddCardFromImageClicked()
            CharactersListUserEvent.CreateCardClicked -> createEmptyCardClicked()
            is CharactersListUserEvent.SearchQueryChanged -> onSearchQueryChanger(event.query)
            is CharactersListUserEvent.ShowCardAvatar -> onShowCardAvatar(event.cardId)
        }
    }

    private fun onCardSelected(cardId: Long) {
        viewModelScope.launch { _uiEvents.emit(CharactersListUiEvent.NavigateToCard(cardId)) }
    }

    private fun addCardFromPng(png: ByteArray) {
        putCardFromImage(png).onEach { resource ->
            when (resource) {
                is Resource.Error -> {
                    _uiEvents.emit(CharactersListUiEvent.SnackbarMessage(resource.message!!))
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    _uiEvents.emit(CharactersListUiEvent.NavigateToCard(resource.data!!))
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
            _uiEvents.emit(CharactersListUiEvent.NavigateToCard(cardId))
        }
    }

    private fun onSearchQueryChanger(query: String) {
        savedStateHandle["query"] = query
        loadCards()
    }

    private fun onShowCardAvatar(cardId: Long) = viewModelScope.launch {
        val card = cards.value.find { it.id == cardId }
        if (card == null) {
            _uiEvents.emit(CharactersListUiEvent.NoSuchCard)
            return@launch
        }
        if (card.photoBytes == null) {
            _uiEvents.emit(CharactersListUiEvent.NoCardPhoto)
            return@launch
        }

        val photoName = getCardPhotoName(card)
        _uiEvents.emit(CharactersListUiEvent.ShowImage(photoName))
    }
}