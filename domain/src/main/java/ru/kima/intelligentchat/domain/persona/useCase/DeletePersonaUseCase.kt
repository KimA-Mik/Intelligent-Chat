package ru.kima.intelligentchat.domain.persona.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository
import ru.kima.intelligentchat.domain.preferences.app.AppPreferencesRepository

class DeletePersonaUseCase(
    private val repository: PersonaRepository,
    private val preferencesRepository: AppPreferencesRepository
) {
    suspend operator fun invoke(persona: Persona): Boolean {
        val count = repository.getPersonasCount()
        if (count < 2) {
            return false
        }

        val selectedPersonaId = preferencesRepository.preferences().first().selectedPersonaId
        if (selectedPersonaId == persona.id) {
            val newId = repository
                .selectPersonas()
                .first()
                .first { it.id != selectedPersonaId }
                .id
            preferencesRepository.updateSelectedPersona(newId)
        }

        repository.deletePersona(persona)
        return true
    }
}