package ru.kima.intelligentchat.presentation.characterCard.charactersList

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
import ru.kima.intelligentchat.domain.card.useCase.GetCardsListUseCase
import ru.kima.intelligentchat.domain.card.useCase.PutCardUseCase
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.useCase.CreatePersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.LoadPersonaImageUseCase
import ru.kima.intelligentchat.domain.persona.useCase.SelectedPersonaUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.SetSelectedPersonaIdUseCase
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUiEvent
import ru.kima.intelligentchat.presentation.characterCard.charactersList.events.CharactersListUserEvent
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.ImmutableCardEntry
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.toImmutable
import ru.kima.intelligentchat.presentation.personas.common.PersonaImageContainer

class CharactersListViewModel(
    private val savedStateHandle: SavedStateHandle,
    preferences: GetPreferencesUseCase,
    private val setSelectedPersonaId: SetSelectedPersonaIdUseCase,
    private val cardsUseCase: GetCardsListUseCase,
    private val putCard: PutCardUseCase,
    private val putCardFromImage: AddCardFromPngUseCase,
    private val createPersona: CreatePersonaUseCase,
    private val loadPersonaImage: LoadPersonaImageUseCase,
    selectedPersona: SelectedPersonaUseCase
) : ViewModel() {
    private val cards = MutableStateFlow(emptyList<ImmutableCardEntry>())
    private val query = savedStateHandle.getStateFlow("query", String())
    private val persona = MutableStateFlow(Persona())
    private val personaImage = MutableStateFlow(PersonaImageContainer())
    private val initialDialog = savedStateHandle.getStateFlow("initialDialog", false)
    private val initialDialogText = savedStateHandle.getStateFlow("initialDialogText", String())

    val state = combine(
        cards,
        query,
        persona,
        personaImage,
        initialDialog,
        initialDialogText
    ) { args ->
        @Suppress("UNCHECKED_CAST")
        CharactersListState(
            cards = args[0] as List<ImmutableCardEntry>,
            searchText = args[1] as String,
            persona = args[2] as Persona,
            personaImage = args[3] as PersonaImageContainer,
            initialDialog = args[4] as Boolean,
            initialDialogText = args[5] as String
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CharactersListState())

    private val _uiEvents = MutableSharedFlow<CharactersListUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        // TODO: redo first launch check 
        preferences().onEach {
            onLoadPersona(it.selectedPersonaId)
        }.launchIn(viewModelScope)

        selectedPersona(viewModelScope)
            .onEach { persona.value = it }
            .onEach { personaImage.value = PersonaImageContainer(loadPersonaImage(it.id).bitmap) }
            .launchIn(viewModelScope)

        loadCards()
    }

    private fun loadCards() = viewModelScope.launch {
        cardsUseCase.filter(query.value)
        cardsUseCase().collect { result ->
            cards.value = result.map {
                it.toImmutable()
            }
        }
    }

    fun onUserEvent(event: CharactersListUserEvent) {
        when (event) {
            is CharactersListUserEvent.EditCardClicked -> onEditCardClicked(event.cardId)
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

    private fun onEditCardClicked(cardId: Long) {
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
        cardsUseCase.filter(query)
    }

    private fun onShowCardAvatar(cardId: Long) = viewModelScope.launch {
        val card = cards.value.find { it.id == cardId }
        if (card == null) {
            _uiEvents.emit(CharactersListUiEvent.Message.NoSuchCard)
            return@launch
        }
        if (card.thumbnail.bitmap == null) {
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
        setSelectedPersonaId(id)
    }

    private fun onMenuButtonClicked() = viewModelScope.launch {
        _uiEvents.emit(CharactersListUiEvent.OpenNavigationDrawer)
    }
}