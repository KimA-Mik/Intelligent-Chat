package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import android.graphics.Bitmap
import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.model.Swipe
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayMessage
import ru.kima.intelligentchat.presentation.common.image.ImmutableBitmap

fun MessageWithSwipes.toDisplayMessage(
    cardName: String,
    cardBitmap: Bitmap? = null,
    personasNames: Map<Long, String> = emptyMap(),
    personasImages: Map<Long, Bitmap?> = emptyMap(),
): DisplayMessage {
    return DisplayMessage(
        messageId = messageId,
        senderName = if (sender == SenderType.Character) cardName else personasNames.getOrElse(
            senderId
        ) { "Unknown persona" },
        senderImage = ImmutableBitmap(
            if (sender == SenderType.Character)
                cardBitmap
            else
                personasImages.getOrDefault(senderId, null)
        ),
        text = swipes.getOrElse(selectedSwipeIndex - 1) { Swipe(text = "OOB") }.text,
        currentSwipe = selectedSwipeIndex,
        totalSwipes = swipes.size
    )
}