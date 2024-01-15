package ru.kima.intelligentchat.presentation.common.image

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable

@Immutable
data class ImmutableBitmap(
    val bitmap: Bitmap? = null
)
