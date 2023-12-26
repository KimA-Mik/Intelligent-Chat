package ru.kima.intelligentchat.domain.persona.useCase

import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository

class UpdatePersonaUseCase(private val repository: PersonaRepository) {
    suspend operator fun invoke(persona: Persona) = repository.updatePersona(persona)
}