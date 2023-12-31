package ru.kima.intelligentchat.presentation.personas.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val personaName = savedStateHandle.getStateFlow(PersonaDetailsField.NAME.name, "")
    private val personaDescription =
        savedStateHandle.getStateFlow(PersonaDetailsField.DESCRIPTION.name, "")

    private var persona = Persona()
    private val personaImage = MutableStateFlow(PersonaImage())

    init {
        val id = savedStateHandle.get<Long>("personaId")!!

        //Justifying usage of saved state handle
        if (isPersonaEmpty()) {
            viewModelScope.launch {
                persona = getPersona(id)
                savedStateHandle[PersonaDetailsField.NAME.name] = persona.name
                savedStateHandle[PersonaDetailsField.DESCRIPTION.name] = persona.description
                personaImage.value = loadPersonaImage(id)
            }
        }
    }

    val state = combine(
        personaName,
        personaDescription,
        personaImage
    ) { personaName, personaDescription, personaImage ->
        PersonaDetailsState(
            personaName = personaName,
            personaDescription = personaDescription,
            personaImage = personaImage
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PersonaDetailsState())

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.UpdatePersonaDetailsField ->
                onUpdatePersonaDetailsField(event.field, event.value)

            UserEvent.SavePersona -> onSavePersona()
        }
    }

    private fun onUpdatePersonaDetailsField(
        field: PersonaDetailsField,
        value: String
    ) {
        savedStateHandle[field.name] = value
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