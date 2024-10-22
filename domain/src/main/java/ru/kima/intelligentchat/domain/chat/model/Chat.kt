package ru.kima.intelligentchat.domain.chat.model

data class Chat(
    val chatId: Long,
    val title: String,
    val cardId: Long,
    val selectedGreeting: Int
)
