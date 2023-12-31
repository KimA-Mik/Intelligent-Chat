package ru.kima.intelligentchat.presentation.personas.details

import ru.kima.intelligentchat.domain.persona.model.PersonaImage

data class PersonaDetailsState(
    val personaName: String = String(),
    val personaDescription: String = String(),
    val personaImage: PersonaImage = PersonaImage()
)
