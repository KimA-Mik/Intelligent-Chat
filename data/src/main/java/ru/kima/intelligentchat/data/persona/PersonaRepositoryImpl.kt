package ru.kima.intelligentchat.data.persona

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.image.dataSource.ImageStorage
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository

class PersonaRepositoryImpl(
    wrapper: DatabaseWrapper,
    private val imageStorage: ImageStorage
) : PersonaRepository {
    private val personaDao = wrapper.database.personaDao()
    override suspend fun insertPersona(persona: Persona): Long {
        val entity = persona.toEntity()
        return personaDao.insertPersona(entity)
    }

    override suspend fun updatePersona(persona: Persona) {
        val entity = persona.toEntity()
        personaDao.updatePersona(entity)
    }

    override fun selectPersona(id: Long): Flow<Persona> {
        return personaDao.selectPersona(id).map { entity ->
            entity.toPersona(imageStorage)
        }
    }

    override fun selectPersonas(): Flow<List<Persona>> {
        return personaDao.selectPersonas().map { list ->
            list.map { entity ->
                entity.toPersona(imageStorage)
            }
        }
    }

    override suspend fun deletePersona(persona: Persona) {
        persona.bitmap?.let {
            val filePath = getPersonaAvatarFileName(persona)
            imageStorage.deleteImage(filePath)
        }
        personaDao.deletePersona(persona.id)
    }
}