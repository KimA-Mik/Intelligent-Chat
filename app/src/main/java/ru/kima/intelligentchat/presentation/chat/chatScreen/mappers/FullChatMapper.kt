package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayChat
import ru.kima.intelligentchat.presentation.common.image.ImmutableImageBitmap

fun FullChat.toDisplayChat(
    card: CharacterCard,
    preloadCardImageBitmap: ImmutableImageBitmap,
    personasNames: Map<Long, String> = emptyMap(),
    personasImages: Map<Long, ImmutableImageBitmap> = emptyMap()
): DisplayChat {
    val firstMessage = MessageWithSwipes(
        messageId = 0,
        chatId = 0,
        sender = SenderType.Character,
        senderId = card.id,
        index = 0,
        selectedSwipeIndex = card.selectedGreeting,
        swipes = card.alternateGreetings.toMutableList()
            .apply { add(0, AltGreeting(id = 0, body = card.firstMes)) }
            .map { it.toSwipe(0) }
    )

    val res = mutableListOf(
        firstMessage.toDisplayMessage(
            card.name,
            preloadCardImageBitmap,
            personasNames,
            personasImages
        )
    )
    res.addAll(messages.map {
        it.toDisplayMessage(
            card.name, preloadCardImageBitmap, personasNames, personasImages
        )
    })
    return DisplayChat(messages = res)
}