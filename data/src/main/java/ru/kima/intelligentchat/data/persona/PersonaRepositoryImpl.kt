package ru.kima.intelligentchat.data.persona

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.image.dataSource.InternalImageStorage
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.model.PersonaImage
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class PersonaRepositoryImpl(
    wrapper: DatabaseWrapper,
    private val imageStorage: InternalImageStorage
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

    override fun subscribeToPersona(id: Long): Flow<Persona?> {
        return personaDao.subscribeToPersona(id)
            .map { entity ->
                entity?.toPersona()
            }
    }

    override suspend fun selectPersona(id: Long): Persona {
        return personaDao.selectPersona(id).toPersona()
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
        val entity = persona
            .toEntity()
            .copy(
                description = String(),
                imageFilePath = null,
                deleted = true
            )
        personaDao.updatePersona(entity)
    }

    override suspend fun loadPersonaImage(id: Long): PersonaImage {
        return try {
            val avatarName = getPersonaAvatarFileName(id)
            val fd = imageStorage.getImageFileDescriptor(avatarName)
                ?: return PersonaImage(bitmap = null)
            val bitmap = BitmapFactory.decodeFileDescriptor(fd)
            PersonaImage(bitmap = bitmap)
        } catch (ex: FileNotFoundException) {
            PersonaImage(bitmap = null)
        }
    }

    override suspend fun updatePersonaImage(id: Long, imageByteArray: ByteArray) {
        val fileName = getPersonaAvatarFileName(id)
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size) ?: return

        val outputStream = ByteArrayOutputStream()
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
            return
        }

        val photoBytes = outputStream.toByteArray()
        imageStorage.saveImage(fileName, photoBytes)
        personaDao.updateImageFilePath(id, fileName)
    }

    override suspend fun getPersonasCount(): Int {
        return personaDao.getPersonasCount()
    }
}