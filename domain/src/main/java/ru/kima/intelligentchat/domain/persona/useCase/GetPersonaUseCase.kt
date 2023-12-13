package ru.kima.intelligentchat.domain.persona.useCase

import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository

class GetPersonaUseCase(
    private val repository: PersonaRepository
) {
    operator fun invoke(id: Long) = repository.selectPersona(id)
}