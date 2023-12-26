package ru.kima.intelligentchat.presentation.personas.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.model.PersonaImage
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.LoadPersonaImageUseCase
import ru.kima.intelligentchat.domain.persona.useCase.UpdatePersonaUseCase
import ru.kima.intelligentchat.presentation.personas.details.events.UserEvent

class PersonaDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    getPersona: GetPersonaUseCase,
    private val loadPersonaImage: LoadPersonaImageUseCase,
    private val updatePersona: UpdatePersonaUseCase
) : ViewModel() {
    //TODO: Make it normal)))
    private val persona: Flow<Persona>
    private val personaImage = MutableStateFlow(PersonaImage())

    init {
        val id = savedStateHandle.get<Long>("personaId")!!
        persona = getPersona(id)
        viewModelScope.launch {
            personaImage.value = loadPersonaImage(id)
        }
    }

    val state = combine(
        persona,
        personaImage
    ) { persona, personaImage ->
        PersonaDetailsState(
            persona = persona,
            personaImage = personaImage
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PersonaDetailsState())

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.UpdatePersonaName -> onUpdatePersonaName(event.name)
            is UserEvent.UpdatePersonaDescription -> onUpdatePersonaDescription(event.description)
        }
    }

    private fun onUpdatePersonaName(name: String) = viewModelScope.launch {
        val newPersona = state.value.persona.copy(name = name)
        updatePersona(newPersona)
    }

    private fun onUpdatePersonaDescription(description: String) = viewModelScope.launch {
        val newPersona = state.value.persona.copy(description = description)
        updatePersona(newPersona)
    }
}