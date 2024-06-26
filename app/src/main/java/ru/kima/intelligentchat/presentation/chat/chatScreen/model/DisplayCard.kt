package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.common.image.ImmutableImageBitmap

@Immutable
data class DisplayCard(
    val name: String = String(),
    val image: ImmutableImageBitmap = ImmutableImageBitmap()
)
