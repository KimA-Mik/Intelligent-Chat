package ru.kima.intelligentchat.presentation.personas.list

import ru.kima.intelligentchat.domain.persona.model.Persona

data class PersonasListState(
    val personas: List<Persona> = emptyList(),
    val query: String = String()
)
