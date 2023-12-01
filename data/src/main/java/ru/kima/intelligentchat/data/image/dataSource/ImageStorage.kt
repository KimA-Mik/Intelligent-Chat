package ru.kima.intelligentchat.data.image.dataSource

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageStorage(
    private val context: Context
) {
    suspend fun saveImage(name: String, bytes: ByteArray): String {
        return withContext(Dispatchers.IO) {
            context.openFileOutput(name, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(bytes)
            }
            ""
        }
    }

    suspend fun getImage(fileName: String): ByteArray {
        return withContext(Dispatchers.IO) {
            context.openFileInput(fileName).use { inputStream ->
                inputStream.readBytes()
            }
        }
    }

    suspend fun deleteImage(fileName: String) {
        return withContext(Dispatchers.IO) {
            context.deleteFile(fileName)
        }
    }
}