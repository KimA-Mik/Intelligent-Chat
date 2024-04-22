package ru.kima.intelligentchat.domain.chat.model

data class Message(
    val messageId: Long,
    val chatId: Long,
    val sender: SenderType,
    val senderId: Long,
    val index: Int,
    val selectedSwipeIndex: Int,
)
