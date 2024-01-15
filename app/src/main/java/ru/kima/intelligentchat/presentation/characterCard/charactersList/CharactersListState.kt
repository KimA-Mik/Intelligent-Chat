package ru.kima.intelligentchat.presentation.characterCard.charactersList

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.presentation.characterCard.charactersList.model.ImmutableCardEntry
import ru.kima.intelligentchat.presentation.personas.common.PersonaImageContainer

@Immutable
data class CharactersListState(
    val cards: List<ImmutableCardEntry> = emptyList(),
    val searchText: String = "",
    val persona: Persona = Persona(),
    val personaImage: PersonaImageContainer = PersonaImageContainer(),
    val initialDialog: Boolean = false,
    val initialDialogText: String = String()
)
