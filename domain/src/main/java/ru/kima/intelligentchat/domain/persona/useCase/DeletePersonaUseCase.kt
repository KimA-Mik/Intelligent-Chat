package ru.kima.intelligentchat.domain.persona.useCase

import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository

class DeletePersonaUseCase(
    private val repository: PersonaRepository
) {
    suspend operator fun invoke(persona: Persona): Boolean {
        val count = repository.getPersonasCount()
        if (count < 2) {
            return false
        }

        repository.deletePersona(persona)
        return true
    }
}