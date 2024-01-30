package ru.kima.intelligentchat.presentation.personas.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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

class PersonaDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getPersona: GetPersonaUseCase,
    private val loadPersonaImage: LoadPersonaImageUseCase,
    private val updatePersona: UpdatePersonaUseCase,
    private val updatePersonaImage: UpdatePersonaImageUseCase,
    private val deletePersona: DeletePersonaUseCase,
    private val tokenizeText: TokenizeTextUseCase
) : ViewModel() {
    private val personaName = savedStateHandle.getStateFlow(PersonaDetailsField.NAME.name, "")
    private val personaDescription =
        savedStateHandle.getStateFlow(PersonaDetailsField.DESCRIPTION.name, "")

    private var persona = Persona()
    private val personaImage = MutableStateFlow(PersonaImageContainer())


    private val personaTokensCount = MutableStateFlow(PersonaTokensCount())

    private val _uiEvents = MutableStateFlow<Event<UiEvent>>(Event(null))
    val uiEvents = _uiEvents.asStateFlow()

    init {
        val id = savedStateHandle.get<Long>("personaId")!!

        //Justifying usage of saved state handle
        if (isPersonaEmpty()) {
            viewModelScope.launch {
                persona = getPersona(id)
                savedStateHandle[PersonaDetailsField.NAME.name] = persona.name
                savedStateHandle[PersonaDetailsField.DESCRIPTION.name] = persona.description
                val image = PersonaImageContainer(loadPersonaImage(id).bitmap)
                personaImage.value = image
                personaTokensCount.value = PersonaTokensCount(
                    nameTokens = tokenizeText(persona.name).size,
                    descriptionTokens = tokenizeText(persona.description).size
                )
            }
        } else {
            personaTokensCount.value = PersonaTokensCount(
                nameTokens = tokenizeText(persona.name).size,
                descriptionTokens = tokenizeText(persona.description).size
            )
        }
    }

    val state = combine(
        personaName,
        personaDescription,
        personaImage,
        personaTokensCount
    ) { personaName, personaDescription, personaImage, personaTokensCount ->
        PersonaDetailsState(
            personaName = personaName,
            personaDescription = personaDescription,
            personaImage = personaImage,
            tokens = personaTokensCount
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PersonaDetailsState())

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.UpdatePersonaDetailsField ->
                onUpdatePersonaDetailsField(event.field, event.value)

            UserEvent.SavePersona -> onSavePersona()
            is UserEvent.UpdatePersonaImage -> onUpdatePersonaImage(event.bytes)
            UserEvent.DeletePersona -> onDeletePersona()
        }
    }

    private fun onUpdatePersonaDetailsField(
        field: PersonaDetailsField,
        value: String
    ) {
        savedStateHandle[field.name] = value
        tokenizeField(field, value)
    }

    private fun tokenizeField(field: PersonaDetailsField, prompt: String) {
        val tokensCount = tokenizeText(prompt).size
        when (field) {
            PersonaDetailsField.NAME -> personaTokensCount.update {
                it.copy(nameTokens = tokensCount)
            }

            PersonaDetailsField.DESCRIPTION -> personaTokensCount.update {
                it.copy(descriptionTokens = tokensCount)
            }
        }
    }

    private fun onSavePersona() = viewModelScope.launch {
        if (persona.id == 0L) {
            return@launch
        }

        persona = persona.copy(
            name = personaName.value,
            description = personaDescription.value
        )

        updatePersona(persona)
        _uiEvents.emit(
            Event(UiEvent.ShowSnackbar(UiEvent.ShowSnackbar.SnackbarMessage.PERSONA_SAVED))
        )
    }

    private fun onUpdatePersonaImage(bytes: ByteArray) = viewModelScope.launch {
        updatePersonaImage(persona.id, bytes)

        val image = PersonaImageContainer(loadPersonaImage(persona.id).bitmap)
        personaImage.value = image

        persona = getPersona(persona.id)
    }

    private fun onDeletePersona() = viewModelScope.launch {
        if (deletePersona(persona)) {
            _uiEvents.emit(Event(UiEvent.ShowSnackbar(UiEvent.ShowSnackbar.SnackbarMessage.PERSONA_DELETED)))
            _uiEvents.emit(Event(UiEvent.PopBack))
        } else {
            _uiEvents.emit(Event(UiEvent.ShowSnackbar(UiEvent.ShowSnackbar.SnackbarMessage.PERSONA_NOT_DELETED)))
        }
    }

    private fun isPersonaEmpty() =
        personaName.value.isEmpty() &&
                personaDescription.value.isEmpty() &&
                personaImage.value.bitmap == null

    enum class PersonaDetailsField {
        NAME,
        DESCRIPTION
    }
}