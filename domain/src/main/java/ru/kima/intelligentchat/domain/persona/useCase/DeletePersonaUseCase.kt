package ru.kima.intelligentchat.domain.persona.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.core.preferences.PreferencesHandler
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository

class DeletePersonaUseCase(
    private val repository: PersonaRepository,
    private val preferences: PreferencesHandler
) {
    suspend operator fun invoke(persona: Persona): Boolean {
        val count = repository.getPersonasCount()
        if (count < 2) {
            return false
        }

        val selectedPersonaId = preferences.data.first().selectedPersonaId
        if (selectedPersonaId == persona.id) {
            val newId = repository
                .selectPersonas()
                .first()
                .first { it.id != selectedPersonaId }
                .id
            preferences.updateSelectedPersona(newId)
        }

        repository.deletePersona(persona)
        return true
    }
}