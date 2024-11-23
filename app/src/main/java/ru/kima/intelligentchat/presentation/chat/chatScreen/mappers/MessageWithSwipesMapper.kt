package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.model.Swipe
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayMessage

fun MessageWithSwipes.toDisplayMessage(
    cardName: String,
    cardImageName: String?,
    personasNames: Map<Long, String> = emptyMap(),
    personasImages: Map<Long, String?> = emptyMap(),
    showSwipeInfo: Boolean = false,
): DisplayMessage {
    val selectedSwipe = swipes.getOrElse(selectedSwipeIndex) {
        Swipe(
            text = "...",
            sendTime = System.currentTimeMillis()
        )
    }
    return DisplayMessage(
        messageId = messageId,
        senderName = if (sender == SenderType.Character) cardName else personasNames.getOrElse(
            senderId
        ) { "Unknown persona" },
        senderImageName = if (sender == SenderType.Character)
            cardImageName
        else
            personasImages[senderId],
        text = selectedSwipe.text,
        sentTimeMillis = selectedSwipe.sendTime,
        currentSwipe = selectedSwipeIndex + 1,
        totalSwipes = swipes.size,
        index = index,
        showSwipeInfo = showSwipeInfo,
    )
}