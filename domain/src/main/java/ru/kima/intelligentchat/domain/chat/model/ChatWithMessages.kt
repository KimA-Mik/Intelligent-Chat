package ru.kima.intelligentchat.domain.chat.model

data class ChatWithMessages(
    val chat: Chat,
    val messages: List<Message>
)
