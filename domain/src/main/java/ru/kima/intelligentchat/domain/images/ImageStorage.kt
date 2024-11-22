package ru.kima.intelligentchat.domain.images

import android.graphics.Bitmap
import java.io.FileDescriptor

interface ImageStorage {
    suspend fun saveImage(name: String, bytes: ByteArray)
    suspend fun getImage(fileName: String): ByteArray
    fun getImageFileDescriptor(fileName: String): FileDescriptor?
    suspend fun getThumbnail(fileName: String): Bitmap
    suspend fun deleteImage(fileName: String)
}
