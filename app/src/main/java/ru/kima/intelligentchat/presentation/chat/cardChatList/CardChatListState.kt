package ru.kima.intelligentchat.presentation.chat.cardChatList

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.chat.cardChatList.model.ChatListItem
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard

@Immutable
data class CardChatListState(
    val displayCard: DisplayCard = DisplayCard(),
    val chats: List<ChatListItem> = emptyList()
)