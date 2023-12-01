package ru.kima.intelligentchat.data.image.repository

import ru.kima.intelligentchat.data.image.dataSource.ImageStorage
import ru.kima.intelligentchat.domain.image.repository.ImageRepository

class ImageRepositoryImpl(
    private val imageStorage: ImageStorage
) : ImageRepository {
    override suspend fun saveImage(name: String, bytes: ByteArray) =
        imageStorage.saveImage(name, bytes)

    override suspend fun getImage(fileName: String) = imageStorage.getImage(fileName)

    override suspend fun deleteImage(fileName: String) = imageStorage.deleteImage(fileName)
}