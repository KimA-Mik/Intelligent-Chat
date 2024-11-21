package ru.kima.intelligentchat.data.image.dataSource

import android.graphics.Bitmap
import java.io.FileDescriptor

class DummyImageStorage : ImageStorage {
    override suspend fun saveImage(name: String, bytes: ByteArray) {}

    override suspend fun getImage(fileName: String): ByteArray {
        return ByteArray(0)
    }

    override fun getImageFileDescriptor(fileName: String): FileDescriptor {
        return FileDescriptor()
    }

    override suspend fun getThumbnail(fileName: String): Bitmap {
        return Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
    }

    override suspend fun deleteImage(fileName: String) {}
}