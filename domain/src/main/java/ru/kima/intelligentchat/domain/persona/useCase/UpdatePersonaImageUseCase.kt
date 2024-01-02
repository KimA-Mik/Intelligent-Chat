package ru.kima.intelligentchat.domain.persona.useCase

import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository

class UpdatePersonaImageUseCase(
    private val repository: PersonaRepository
) {
    suspend operator fun invoke(personaId: Long, imageByteArray: ByteArray) {
        try {
            repository.updatePersonaImage(personaId, imageByteArray)
        } catch (e: Exception) {
            e.message?.let {

            }
        }
    }
}