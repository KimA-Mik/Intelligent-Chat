package ru.kima.intelligentchat.presentation.personas.details

import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.model.PersonaImage

data class PersonaDetailsState(
    val persona: Persona = Persona(),
    val personaImage: PersonaImage = PersonaImage()
)
