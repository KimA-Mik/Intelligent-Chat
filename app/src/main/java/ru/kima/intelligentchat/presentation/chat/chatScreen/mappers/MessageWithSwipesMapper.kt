package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.model.Swipe
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayMessage
import ru.kima.intelligentchat.presentation.common.image.ImmutableImageBitmap

fun MessageWithSwipes.toDisplayMessage(
    cardName: String,
    cardBitmap: ImmutableImageBitmap = ImmutableImageBitmap(),
    personasNames: Map<Long, String> = emptyMap(),
    personasImages: Map<Long, ImmutableImageBitmap> = emptyMap(),
): DisplayMessage {
    return DisplayMessage(
        messageId = messageId,
        senderName = if (sender == SenderType.Character) cardName else personasNames.getOrElse(
            senderId
        ) { "Unknown persona" },
        senderImage = if (sender == SenderType.Character)
            cardBitmap
        else
            personasImages.getOrElse(senderId) { ImmutableImageBitmap() },
        text = swipes.getOrElse(selectedSwipeIndex - 1) { Swipe(text = "OOB") }.text,
        currentSwipe = selectedSwipeIndex,
        totalSwipes = swipes.size
    )
}