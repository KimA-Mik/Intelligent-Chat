package ru.kima.intelligentchat.data.chat.mappers

import ru.kima.intelligentchat.data.chat.entities.ChatEntity
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.model.Message

fun ChatEntity.toChat(messages: List<Message>): Chat {
    return Chat(
        chatId = chatId,
        title = title,
        cardId = cardId,
        messages = messages,
    )
}

fun Chat.toEntity(): ChatEntity {
    return ChatEntity(
        chatId = chatId,
        title = title,
        cardId = cardId,
    )
}