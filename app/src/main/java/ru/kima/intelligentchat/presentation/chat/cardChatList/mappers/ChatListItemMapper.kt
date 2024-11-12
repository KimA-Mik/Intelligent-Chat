package ru.kima.intelligentchat.presentation.chat.cardChatList.mappers

import ru.kima.intelligentchat.domain.chat.model.ChatWithMessages
import ru.kima.intelligentchat.presentation.chat.cardChatList.model.ChatListItem

fun ChatWithMessages.toListItem(selected: Boolean): ChatListItem {
    return ChatListItem(
        id = chat.chatId,
        name = chat.title,
        selected = selected
    )
}