package ru.kima.intelligentchat.domain.persona.useCase

import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository

class GetPersonasUseCase(
    private val repository: PersonaRepository

) {
    operator fun invoke(query: String = String()) = repository.selectPersonas().map { personas ->
        if (query.isNotBlank()) {
            personas.filter { it.name.contains(query, ignoreCase = true) }
        } else {
            personas
        }
    }
}