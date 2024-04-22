package ru.kima.intelligentchat.domain.chat.model

data class Swipe(
    val swipeId: Long,
    val messageId: Long,
    val text: String
)
