package ru.kima.intelligentchat.presentation.characterCard.charactersList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
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
import ru.kima.intelligentchat.core.preferences.PreferencesHandler
import ru.kima.intelligentchat.domain.card.model.CardEntry
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.AddCardFromPngUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardsListUseCase
import ru.kima.intelligentchat.domain.card.useCase.PutCardUseCase
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.useCase.CreatePersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.SubscribeToPersonaUseCase
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUiEvent
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUserEvent

class CharactersListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val preferencesHandler: PreferencesHandler,
    private val getCards: GetCardsListUseCase,
    private val putCard: PutCardUseCase,
    private val putCardFromImage: AddCardFromPngUseCase,
    private val createPersona: CreatePersonaUseCase,
    private val subscribeToPersona: SubscribeToPersonaUseCase
) : ViewModel() {
    private val cards = MutableStateFlow(emptyList<CardEntry>())
    private val query = savedStateHandle.getStateFlow("query", String())
    private val persona = MutableStateFlow(Persona())
    private val initialDialog = savedStateHandle.getStateFlow("initialDialog", false)
    private val initialDialogText = savedStateHandle.getStateFlow("initialDialogText", String())

    //TODO: Explore better implementation
    private var cardsJob: Job? = null
    private var personaJob: Job? = null

    val state = combine(
        cards,
        query,
        persona,
        initialDialog,
        initialDialogText
    ) { cards, query, persona, initialDialog, initialDialogText ->
        CharactersListState(
            cards = cards,
            searchText = query,
            persona = persona,
            initialDialog = initialDialog,
            initialDialogText = initialDialogText
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CharactersListState())

    private val _uiEvents = MutableSharedFlow<CharactersListUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        preferencesHandler.data.onEach { preferences ->
            onLoadPersona(preferences.selectedPersonaId)
        }.launchIn(viewModelScope)

        loadCards()
    }

    private fun loadCards() {
        cardsJob?.cancel()
        cardsJob = viewModelScope.launch {
            getCards(query.value).collect { result ->
                cards.value = result
            }
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
            is CharactersListUserEvent.InitDialogValueChanged -> onInitDialogValueChanged(event.newValue)
            CharactersListUserEvent.AcceptInitialPersonaName -> onAcceptInitialPersonaName()
            CharactersListUserEvent.DismissInitialPersonaName -> onDismissInitialPersonaName()
            CharactersListUserEvent.OnMenuButtonClicked -> onMenuButtonClicked()
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
            _uiEvents.emit(CharactersListUiEvent.Message.NoSuchCard)
            return@launch
        }
        if (card.thumbnail == null) {
            _uiEvents.emit(CharactersListUiEvent.Message.NoCardPhoto)
            return@launch
        }

        _uiEvents.emit(CharactersListUiEvent.ShowCardImage(cardId))
    }

    private fun onInitDialogValueChanged(newValue: String) {
        savedStateHandle["initialDialogText"] = newValue
    }

    private fun onLoadPersona(personaId: Long) {
        if (personaId == 0L) {
            savedStateHandle["initialDialog"] = true
            return
        }

        if (personaId == persona.value.id) {
            return
        }

        personaJob?.cancel()
        personaJob = subscribeToPersona(personaId).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    persona.emit(resource.data!!)
                }

                is Resource.Error -> {
                    preferencesHandler.updateData {
                        it.copy(selectedPersonaId = resource.data!!.id)
                    }
                }

                is Resource.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun onDismissInitialPersonaName() = viewModelScope.launch {
        _uiEvents.emit(CharactersListUiEvent.Message.DefaultPersonaInit)
        onInitDialogResult("User")
    }

    private fun onAcceptInitialPersonaName() = viewModelScope.launch {
        onInitDialogResult(initialDialogText.value)
    }

    private suspend fun onInitDialogResult(personaName: String) {
        savedStateHandle["initialDialog"] = false
        val persona = Persona(name = personaName)
        val id = createPersona(persona)
        preferencesHandler.updateData {
            it.copy(selectedPersonaId = id)
        }
    }

    private fun onMenuButtonClicked() = viewModelScope.launch {
        _uiEvents.emit(CharactersListUiEvent.OpenNavigationDrawer)
    }
}