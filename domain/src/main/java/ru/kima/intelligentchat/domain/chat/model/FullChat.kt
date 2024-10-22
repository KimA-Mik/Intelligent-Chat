package ru.kima.intelligentchat.domain.chat.model

data class FullChat(
    val chatId: Long = 0,
    val title: String = String(),
    val cardId: Long = 0,
    val selectedGreeting: Int = 0,
    val messages: List<MessageWithSwipes> = emptyList()
) {
    companion object {
        fun fromChatAndMessages(chat: Chat, messages: List<MessageWithSwipes>): FullChat {
            return FullChat(
                chatId = chat.chatId,
                title = chat.title,
                cardId = chat.cardId,
                selectedGreeting = chat.selectedGreeting,
                messages = messages
            )
        }
    }
}
