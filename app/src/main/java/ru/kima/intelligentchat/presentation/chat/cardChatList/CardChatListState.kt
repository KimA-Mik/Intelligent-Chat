package ru.kima.intelligentchat.presentation.chat.cardChatList

import ru.kima.intelligentchat.presentation.chat.cardChatList.model.ChatListItem
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard

data class CardChatListState(
    val displayCard: DisplayCard = DisplayCard(),
    val chats: List<ChatListItem> = emptyList()
)