package ru.kima.intelligentchat.presentation.personas.list.model

import ru.kima.intelligentchat.domain.persona.model.Persona

data class PersonaItem(
    val id: Long = 0,
    val name: String = String(),
    val description: String = String(),
    val selected: Boolean = false
)

fun Persona.toListItem(
    selected: Boolean
): PersonaItem {
    return PersonaItem(
        id = id,
        name = name,
        description = description,
        selected = selected
    )
}
