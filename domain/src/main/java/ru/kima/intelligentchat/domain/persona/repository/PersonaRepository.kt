package ru.kima.intelligentchat.domain.persona.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.model.PersonaImage

interface PersonaRepository {
    suspend fun insertPersona(persona: Persona): Long
    suspend fun updatePersona(persona: Persona)
    fun selectPersona(id: Long): Flow<Persona>
    fun selectPersonas(): Flow<List<Persona>>
    suspend fun deletePersona(persona: Persona)
    suspend fun loadPersonaImage(id: Long): PersonaImage
}