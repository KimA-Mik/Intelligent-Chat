package ru.kima.intelligentchat.data.persona

import android.graphics.BitmapFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.image.dataSource.ImageStorage
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.model.PersonaImage
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository
import java.io.FileNotFoundException

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
            entity.toPersona()
        }
    }

    override fun selectPersonas(): Flow<List<Persona>> {
        return personaDao.selectPersonas().map { list ->
            list.map { entity ->
                entity.toPersona()
            }
        }
    }

    override suspend fun deletePersona(persona: Persona) {
        persona.imageName?.let {
            imageStorage.deleteImage(it)
        }
        personaDao.deletePersona(persona.id)
    }

    override suspend fun loadPersonaImage(id: Long): PersonaImage {
        return try {
            val avatarName = getPersonaAvatarFileName(id)
            val fd = imageStorage.getImageFileDescriptor(avatarName)
            val bitmap = BitmapFactory.decodeFileDescriptor(fd)
            PersonaImage(bitmap = bitmap)
        } catch (ex: FileNotFoundException) {
            PersonaImage(bitmap = null)
        }
    }
}