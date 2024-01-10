package ru.kima.intelligentchat.presentation.personas.list

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.personas.common.PersonaImageContainer
import ru.kima.intelligentchat.presentation.personas.list.model.PersonaItem

@Immutable
data class PersonasListState(
    val personas: List<PersonaItem> = emptyList(),
    val thumbnails: List<PersonaImageContainer> = emptyList(),
    val query: String = String()
)
