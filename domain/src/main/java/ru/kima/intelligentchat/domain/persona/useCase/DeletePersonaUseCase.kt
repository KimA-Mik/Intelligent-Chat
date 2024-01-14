package ru.kima.intelligentchat.domain.persona.useCase

import kotlinx.coroutines.flow.last
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

        val selectedPersonaId = preferences.data.last().selectedPersonaId
        if (selectedPersonaId == persona.id) {
            val newId = repository
                .selectPersonas()
                .last()
                .first { it.id != selectedPersonaId }
                .id
            preferences.updateSelectedPersona(newId)
        }

        repository.deletePersona(persona)
        return true
    }
}