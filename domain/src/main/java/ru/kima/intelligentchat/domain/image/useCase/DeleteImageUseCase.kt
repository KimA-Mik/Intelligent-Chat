package ru.kima.intelligentchat.domain.image.useCase

import ru.kima.intelligentchat.domain.image.repository.ImageRepository

class DeleteImageUseCase(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(name: String) {
        repository.deleteImage(name)
    }
}