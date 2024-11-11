package ru.kima.intelligentchat.data.chat.mappers

import ru.kima.intelligentchat.data.chat.dto.SimpleChatWithMessagesDto
import ru.kima.intelligentchat.domain.chat.model.ChatWithMessages

fun SimpleChatWithMessagesDto.toChatWithMessages(): ChatWithMessages {
    return ChatWithMessages(
        chat = chat.toChat(),
        messages = messages.map { it.toMessage() }
    )
}

