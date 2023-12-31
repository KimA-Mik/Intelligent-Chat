package ru.kima.intelligentchat.domain.persona.useCase

import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository

class SubscribeToPersonaUseCase(
    private val repository: PersonaRepository
) {
    operator fun invoke(id: Long) = repository.subscribeToPersona(id)
}