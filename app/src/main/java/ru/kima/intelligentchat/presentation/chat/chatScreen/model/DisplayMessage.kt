package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import android.icu.util.Calendar
import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.common.image.ImmutableBitmap

@Immutable
data class DisplayMessage(
    val messageId: Long = 0,
    val senderName: String = String(),
    val senderImage: ImmutableBitmap = ImmutableBitmap(),
    val text: String = String(),
    val sentTimeMillis: Long = Calendar.getInstance().time.time,
    val currentSwipe: Int = 1,
    val totalSwipes: Int = 1,
)