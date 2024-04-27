package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.common.image.ImmutableBitmap

@Immutable
data class DisplayCard(
    val name: String = String(),
    val image: ImmutableBitmap = ImmutableBitmap()
)
