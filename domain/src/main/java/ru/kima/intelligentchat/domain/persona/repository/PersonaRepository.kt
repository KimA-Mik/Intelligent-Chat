package ru.kima.intelligentchat.domain.persona.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.model.PersonaImage

interface PersonaRepository {
    suspend fun insertPersona(persona: Persona): Long
    suspend fun updatePersona(persona: Persona)
    fun subscribeToPersona(id: Long): Flow<Persona?>
    suspend fun selectPersona(id: Long): Persona
    fun selectPersonas(): Flow<List<Persona>>
    suspend fun deletePersona(persona: Persona)
    suspend fun loadPersonaImage(id: Long): PersonaImage
    suspend fun updatePersonaImage(id: Long, imageByteArray: ByteArray)
    suspend fun getPersonasCount(): Int
}