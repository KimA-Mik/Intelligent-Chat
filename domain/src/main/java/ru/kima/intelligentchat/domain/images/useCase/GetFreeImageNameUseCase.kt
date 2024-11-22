package ru.kima.intelligentchat.domain.images.useCase

import ru.kima.intelligentchat.domain.images.ImageStorage
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetFreeImageNameUseCase(
    private val imageStorage: ImageStorage
) {
    @OptIn(ExperimentalUuidApi::class)
    operator fun invoke(): String {
        var name = Uuid.random().toString() + EXTENSION
        while (imageStorage.getImageFileDescriptor(name) != null) {
            name = Uuid.random().toString() + EXTENSION
        }

        return name
    }

    companion object {
        private const val EXTENSION = ".png"
    }
}