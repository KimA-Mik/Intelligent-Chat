package ru.kima.intelligentchat.data.chat.mappers

import ru.kima.intelligentchat.data.chat.dto.ChatWithMessagesDto
import ru.kima.intelligentchat.domain.chat.model.FullChat

fun ChatWithMessagesDto.toFullChat(): FullChat {
    return FullChat.fromChatAndMessages(
        chat = chat.toChat(),
        messages = messages.map { it.toMessage() }
    )
}