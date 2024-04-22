package ru.kima.intelligentchat.domain.chat.model

data class FullChat(
    val chatId: Long,
    val title: String,
    val cardId: Long,
    val messages: List<MessageWithSwipes>
) {
    companion object {
        fun fromChatAndMessages(chat: Chat, messages: List<MessageWithSwipes>): FullChat {
            return FullChat(
                chatId = chat.chatId,
                title = chat.title,
                cardId = chat.cardId,
                messages = messages
            )
        }
    }
}
