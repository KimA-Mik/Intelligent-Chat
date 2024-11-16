package ru.kima.intelligentchat.domain.chat.model

data class Swipe(
    val swipeId: Long = 0,
    val messageId: Long = 0,
    val text: String = String(),
    val deleted: Boolean = false,
    val sendTime: Long,
)
