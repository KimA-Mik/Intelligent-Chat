package ru.kima.intelligentchat.data.chat.mappers

import ru.kima.intelligentchat.data.chat.entities.ChatEntity
import ru.kima.intelligentchat.domain.chat.model.Chat

fun ChatEntity.toChat(): Chat {
    return Chat(
        chatId = chatId,
        title = title,
        cardId = cardId,
        selectedGreeting = selectedGreeting
    )
}

fun Chat.toEntity(): ChatEntity {
    return ChatEntity(
        chatId = chatId,
        title = title,
        cardId = cardId,
        selectedGreeting = selectedGreeting
    )
}