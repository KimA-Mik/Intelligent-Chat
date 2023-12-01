package ru.kima.intelligentchat.domain.image.repository

interface ImageRepository {
    suspend fun saveImage(name: String, bytes: ByteArray): String
    suspend fun getImage(fileName: String): ByteArray
    suspend fun deleteImage(fileName: String)
}