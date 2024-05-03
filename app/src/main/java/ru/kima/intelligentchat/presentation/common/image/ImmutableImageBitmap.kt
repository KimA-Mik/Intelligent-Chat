package ru.kima.intelligentchat.presentation.common.image

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.ImageBitmap

@Immutable
data class ImmutableImageBitmap(
    val imageBitmap: ImageBitmap? = null
)
