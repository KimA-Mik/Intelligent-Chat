package ru.kima.intelligentchat.presentation.characterCard.charactersList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.AddCardFromPngUseCase
import ru.kima.intelligentchat.domain.card.useCase.DeleteCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardsListUseCase
import ru.kima.intelligentchat.domain.card.useCase.PutCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.RestoreCardUseCase
import ru.kima.intelligentchat.domain.common.Resource
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.useCase.CreatePersonaUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.SetSelectedPersonaIdUseCase
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUiEvent
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUserEvent
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.ImmutableCardEntry
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.toImmutable

class CharactersListViewModel(
    private val savedStateHandle: SavedStateHandle,
    preferences: GetPreferencesUseCase,
    private val putCard: PutCardUseCase,
    private val deleteCard: DeleteCardUseCase,
    private val restoreCard: RestoreCardUseCase,
    private val cardsUseCase: GetCardsListUseCase,
    private val createPersona: CreatePersonaUseCase,
    private val putCardFromImage: AddCardFromPngUseCase,
    private val setSelectedPersonaId: SetSelectedPersonaIdUseCase,
) : ViewModel() {
    private val cards = MutableStateFlow(emptyList<ImmutableCardEntry>())
    private val query = savedStateHandle.getStateFlow<String?>(QUERY_KEY, null)
    private val initialDialog = savedStateHandle.getStateFlow(INITIAL_DIALOG_KEY, false)
    private val initialDialogText = savedStateHandle.getStateFlow(INITIAL_DIALOG_TEXT_KEY, String())

    val state = combine(
        cards.map { it.toImmutableList() },
        query,
        initialDialog,
        initialDialogText
    ) { cards,
        query,
        initialDialog,
        initialDialogText ->
        CharactersListState(
            cards = cards,
            searchText = query,
            initialDialog = initialDialog,
            initialDialogText = initialDialogText,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CharactersListState())

    private val _uiEvents = MutableStateFlow(Event<CharactersListUiEvent>(null))
    val uiEvents = _uiEvents.asStateFlow()

    init {
        // TODO: redo first launch check 
        preferences().onEach {
            onLoadPersona(it.selectedPersonaId)
        }.launchIn(viewModelScope)

        loadCards()
    }

    private fun loadCards() = viewModelScope.launch {
        query.value?.let {
            cardsUseCase.filter(it)
        }

        cardsUseCase().collect { result ->
            cards.value = result.map {
                it.toImmutable()
            }
        }
    }

    fun onUserEvent(event: CharactersListUserEvent) {
        when (event) {
            is CharactersListUserEvent.EditCardClicked -> onEditCardClicked(event.cardId)
            is CharactersListUserEvent.DeleteCardClicked -> onDeleteCardClicked(event.cardId)
            is CharactersListUserEvent.RestoreCardClicked -> onRestoreCardClicked(event.cardId)
            is CharactersListUserEvent.OpenCardChat -> onOpenCardChat(event.cardId)
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

    private fun onEditCardClicked(cardId: Long) = viewModelScope.launch {
        _uiEvents.emit(Event(CharactersListUiEvent.NavigateToCardEdit(cardId)))
    }

    private fun onDeleteCardClicked(cardId: Long) = viewModelScope.launch {
        deleteCard(cardId)
        _uiEvents.value = Event(CharactersListUiEvent.CardDeleted(cardId))
    }

    private fun onRestoreCardClicked(cardId: Long) = viewModelScope.launch {
        restoreCard(cardId)
    }

    private fun onOpenCardChat(cardId: Long) = viewModelScope.launch {
        _uiEvents.emit(Event(CharactersListUiEvent.NavigateToCardChat(cardId)))
    }

    private fun addCardFromPng(png: ByteArray) {
        putCardFromImage(png).onEach { resource ->
            when (resource) {
                is Resource.Error -> {
                    _uiEvents.emit(Event(CharactersListUiEvent.SnackbarMessage(resource.message!!)))
                }

                is Resource.Success -> {
                    _uiEvents.emit(Event(CharactersListUiEvent.NavigateToCardEdit(resource.data!!)))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun onAddCardFromImageClicked() {
        viewModelScope.launch {
            _uiEvents.emit(Event(CharactersListUiEvent.SelectPngImage))
        }
    }

    private fun createEmptyCardClicked() {
        viewModelScope.launch {
            val cardId = putCard(CharacterCard.default())
            _uiEvents.emit(Event(CharactersListUiEvent.NavigateToCardEdit(cardId)))
        }
    }

    private fun onSearchQueryChanger(query: String?) {
        savedStateHandle[QUERY_KEY] = query
        if (query == null) {
            cardsUseCase.filter("")
        } else {
            cardsUseCase.filter(query)

        }
    }

    private fun onShowCardAvatar(cardId: Long) = viewModelScope.launch {
        val card = cards.value.find { it.id == cardId }
        if (card == null) {
            _uiEvents.emit(Event(CharactersListUiEvent.Message.NoSuchCard))
            return@launch
        }
        if (card.photoName == null) {
            _uiEvents.emit(Event(CharactersListUiEvent.Message.NoCardPhoto))
            return@launch
        }

        _uiEvents.emit(Event(CharactersListUiEvent.ShowCardImage(cardId)))
    }

    private fun onInitDialogValueChanged(newValue: String) {
        savedStateHandle[INITIAL_DIALOG_TEXT_KEY] = newValue
    }

    private fun onLoadPersona(personaId: Long) {
        if (personaId == 0L) {
            savedStateHandle[INITIAL_DIALOG_KEY] = true
            return
        }
    }

    private fun onDismissInitialPersonaName() = viewModelScope.launch {
        _uiEvents.emit(Event(CharactersListUiEvent.Message.DefaultPersonaInit))
        onInitDialogResult("User")
    }

    private fun onAcceptInitialPersonaName() = viewModelScope.launch {
        onInitDialogResult(initialDialogText.value)
    }

    private suspend fun onInitDialogResult(personaName: String) {
        savedStateHandle[INITIAL_DIALOG_KEY] = false
        val persona = Persona(name = personaName)
        val id = createPersona(persona)
        setSelectedPersonaId(id)
    }

    private fun onMenuButtonClicked() = viewModelScope.launch {
        _uiEvents.emit(Event(CharactersListUiEvent.OpenNavigationDrawer))
    }

    companion object {
        private const val QUERY_KEY = "query"
        private const val INITIAL_DIALOG_KEY = "initialDialog"
        private const val INITIAL_DIALOG_TEXT_KEY = "initialDialogText"
    }
}