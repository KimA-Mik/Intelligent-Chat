package ru.kima.intelligentchat.domain.persona.useCase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository

class SubscribeToPersonaUseCase(
    private val repository: PersonaRepository
) {
    operator fun invoke(id: Long): Flow<Resource<Persona>> =
        repository.subscribeToPersona(id).map { persona ->
            if (persona != null) {
                Resource.Success(persona)
            } else {
                Resource.Error(data = repository.selectPersonas().first().first(), message = "")
            }
        }
}