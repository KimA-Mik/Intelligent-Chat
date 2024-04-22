package ru.kima.intelligentchat.domain.chat.model

data class FullChat(
    val chatId: Long,
    val title: String,
    val cardId: Long,
    val messages: List<MessageWithSwipes>
)
