package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import android.icu.util.Calendar
import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.presentation.common.image.ImmutableImageBitmap

@Immutable
data class DisplayMessage(
    val messageId: Long = 0,
    val senderName: String = String(),
    val senderImage: ImmutableImageBitmap = ImmutableImageBitmap(),
    val senderType: SenderType = SenderType.Character,
    val text: String = String(),
    val sentTimeMillis: Long = Calendar.getInstance().time.time,
    val currentSwipe: Int = 1,
    val totalSwipes: Int = 1,
    val index: Int = 0,
    val showSwipeInfo: Boolean = false,
)