package ru.kima.intelligentchat.data.image.dataSource

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Build
import android.util.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kima.intelligentchat.domain.images.ImageStorage
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.io.path.Path


class InternalImageStorage(
    private val context: Context
) : ImageStorage {
    override suspend fun saveImage(name: String, bytes: ByteArray): Boolean {
        val success = withContext(Dispatchers.Default) {
            val bitmap =
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return@withContext false
            val outputStream = ByteArrayOutputStream()
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                return@withContext false
            }
            val encodedBytes = outputStream.toByteArray()

            withContext(Dispatchers.IO) {
                context.openFileOutput(name, Context.MODE_PRIVATE).use { outputStream ->
                    outputStream.write(encodedBytes)
                }
            }
            return@withContext true
        }

        if (success && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            saveLegacyThumbnail(name, bytes)
        }

        return success
    }

    override suspend fun getImage(fileName: String): ByteArray {
        return withContext(Dispatchers.IO) {
            context.openFileInput(fileName).use { inputStream ->
                inputStream.readBytes()
            }
        }
    }

    override fun getImageFileDescriptor(fileName: String): FileDescriptor? {
        return try {
            context.openFileInput(fileName).fd
        } catch (_: FileNotFoundException) {
            null
        } catch (_: IOException) {
            null
        }
    }

    override suspend fun getThumbnail(fileName: String): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            withContext(Dispatchers.IO) {
                ThumbnailUtils.createImageThumbnail(
                    Path(context.filesDir.path, fileName).toFile(),
                    Size(256, 256),
                    null
                )
            }
        } else {
            withContext(Dispatchers.IO) {
                context.openFileInput(getThumbnailName(fileName)).use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            }
        }
    }

    override suspend fun deleteImage(fileName: String) {
        withContext(Dispatchers.IO) {
            context.deleteFile(fileName)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                context.deleteFile(getThumbnailName(fileName))
            }
        }
    }

    private fun getThumbnailName(originalName: String) = "thumbnail_$originalName"

    private suspend fun saveLegacyThumbnail(name: String, bytes: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        val thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 256, 256)
        withContext(Dispatchers.IO) {
            context.openFileOutput(getThumbnailName(name), Context.MODE_PRIVATE)
                .use { outputStream ->
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
        }
    }
}