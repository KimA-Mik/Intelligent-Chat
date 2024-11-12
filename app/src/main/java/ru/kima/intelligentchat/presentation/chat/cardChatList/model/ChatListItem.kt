package ru.kima.intelligentchat.presentation.chat.cardChatList.model

import androidx.compose.runtime.Immutable

@Immutable
data class ChatListItem(
    val id: Long = 0,
    val name: String = "",
    val selected: Boolean = false
)
