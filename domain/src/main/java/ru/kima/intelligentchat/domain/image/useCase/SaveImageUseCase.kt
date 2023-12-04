package ru.kima.intelligentchat.domain.image.useCase

import ru.kima.intelligentchat.domain.image.repository.ImageRepository

class SaveImageUseCase(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(name: String, bytes: ByteArray) {
        repository.saveImage(name, bytes)
    }
}