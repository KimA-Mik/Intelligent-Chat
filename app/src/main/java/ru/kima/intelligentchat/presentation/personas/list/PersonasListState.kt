package ru.kima.intelligentchat.presentation.personas.list

import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.model.PersonaImage

data class PersonasListState(
    val personas: List<Persona> = emptyList(),
    val thumbnails: List<PersonaImage?> = emptyList(),
    val query: String = String(),
)
