package ru.kima.intelligentchat.presentation.personas.details

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.personas.common.PersonaImageContainer
import ru.kima.intelligentchat.presentation.personas.details.model.PersonaTokensCount

@Immutable
data class PersonaDetailsState(
    val personaName: String = String(),
    val personaDescription: String = String(),
    val personaImage: PersonaImageContainer = PersonaImageContainer(),
    val tokens: PersonaTokensCount = PersonaTokensCount()
)
