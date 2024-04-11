package ru.kima.intelligentchat.presentation.personas.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.useCase.DeletePersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.LoadPersonaImageUseCase
import ru.kima.intelligentchat.domain.persona.useCase.UpdatePersonaImageUseCase
import ru.kima.intelligentchat.domain.persona.useCase.UpdatePersonaUseCase
import ru.kima.intelligentchat.domain.tokenizer.useCase.TokenizeTextUseCase
import ru.kima.intelligentchat.presentation.personas.common.PersonaImageContainer
import ru.kima.intelligentchat.presentation.personas.details.events.UiEvent
import ru.kima.intelligentchat.presentation.personas.details.events.UserEvent
import ru.kima.intelligentchat.presentation.personas.details.model.PersonaTokensCount

@OptIn(FlowPreview::class)
class PersonaDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getPersona: GetPersonaUseCase,
    private val loadPersonaImage: LoadPersonaImageUseCase,
    private val updatePersona: UpdatePersonaUseCase,
    private val updatePersonaImage: UpdatePersonaImageUseCase,
    private val deletePersona: DeletePersonaUseCase,
    private val tokenizeText: TokenizeTextUseCase
) : ViewModel() {
    private val _persona = MutableStateFlow(Persona())
    private val _personaImage = MutableStateFlow(PersonaImageContainer())
    private val _personaTokensCount = MutableStateFlow(PersonaTokensCount())

    private var deleted = false

    private val _uiEvents = MutableStateFlow<Event<UiEvent>>(Event(null))
    val uiEvents = _uiEvents.asStateFlow()

    val state = combine(
        _persona,
        _personaImage,
        _personaTokensCount
    ) { persona,
        personaImage,
        personaTokensCount ->
        PersonaDetailsState(
            personaName = persona.name,
            personaDescription = persona.description,
            personaImage = personaImage,
            tokens = personaTokensCount
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PersonaDetailsState())

    init {
        val id = savedStateHandle.get<Long>("personaId")
        viewModelScope.launch {
            if (id == null || id == 0L) {
                _uiEvents.emit(Event(UiEvent.PopBack))
                return@launch
            }
            _persona.value = getPersona(id)
            _personaImage.value = PersonaImageContainer(loadPersonaImage(id).bitmap)

            _personaTokensCount.value = PersonaTokensCount(
                nameTokens = tokenizeText(_persona.value.name).size,
                descriptionTokens = tokenizeText(_persona.value.description).size
            )

            _persona
                .debounce(1000)
                .filter { !deleted }
                .collect { persona ->
                    updatePersona(persona)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        MainScope().launch(Dispatchers.IO) {
            if (deleted) return@launch

            val persona = _persona.value
            if (persona.id == 0L) return@launch
            updatePersona(persona)
        }
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.UpdatePersonaDetailsField ->
                onUpdatePersonaDetailsField(event.field, event.value)

            is UserEvent.UpdatePersonaImage -> onUpdatePersonaImage(event.bytes)
            UserEvent.DeletePersona -> onDeletePersona()
        }
    }

    private fun onUpdatePersonaDetailsField(
        field: PersonaDetailsField,
        value: String
    ) {
        _persona.update {
            when (field) {
                PersonaDetailsField.NAME -> it.copy(name = value)
                PersonaDetailsField.DESCRIPTION -> it.copy(description = value)
            }
        }
        tokenizeField(field, value)
    }

    private fun tokenizeField(field: PersonaDetailsField, prompt: String) {
        val tokensCount = tokenizeText(prompt).size
        when (field) {
            PersonaDetailsField.NAME -> _personaTokensCount.update {
                it.copy(nameTokens = tokensCount)
            }

            PersonaDetailsField.DESCRIPTION -> _personaTokensCount.update {
                it.copy(descriptionTokens = tokensCount)
            }
        }
    }

    private fun onUpdatePersonaImage(bytes: ByteArray) = viewModelScope.launch {
        val persona = _persona.value
        updatePersonaImage(persona.id, bytes)

        val image = PersonaImageContainer(loadPersonaImage(persona.id).bitmap)
        _personaImage.value = image

        _persona.value = getPersona(persona.id)
    }

    private fun onDeletePersona() = viewModelScope.launch {
        deleted = true
        val persona = _persona.value
        if (deletePersona(persona)) {
            _uiEvents.emit(Event(UiEvent.ShowSnackbar(UiEvent.ShowSnackbar.SnackbarMessage.PERSONA_DELETED)))
            _uiEvents.emit(Event(UiEvent.PopBack))
        } else {
            _uiEvents.emit(Event(UiEvent.ShowSnackbar(UiEvent.ShowSnackbar.SnackbarMessage.PERSONA_NOT_DELETED)))
        }
    }


    enum class PersonaDetailsField {
        NAME,
        DESCRIPTION
    }
}