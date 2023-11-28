package ru.kima.intelligentchat.presentation.common.image

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

class ImagePicker(private val context: Context) {
    private lateinit var getContent: ActivityResultLauncher<String>

    @Composable
    fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
        getContent = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                context.contentResolver.openInputStream(uri)?.use {
                    onImagePicked(it.readBytes())
                }
            }
        }
    }

    fun pickImage(imagesType: String = "image/*") {
        getContent.launch(imagesType)
    }
}