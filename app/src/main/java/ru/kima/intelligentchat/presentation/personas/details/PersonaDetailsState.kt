package ru.kima.intelligentchat.presentation.personas.details

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.personas.common.PersonaImageContainer

@Immutable
data class PersonaDetailsState(
    val personaName: String = String(),
    val personaDescription: String = String(),
    val personaImage: PersonaImageContainer = PersonaImageContainer()
)
