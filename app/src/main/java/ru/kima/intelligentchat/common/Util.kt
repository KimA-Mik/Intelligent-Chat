package ru.kima.intelligentchat.common

import android.content.Context
import java.io.File

fun Context.photoNameToFile(photoName: String?): File? {
    if (photoName == null) return null

    return File(filesDir.absolutePath, photoName)
}