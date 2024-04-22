package ru.kima.intelligentchat.domain.chat.model

data class MessageWithSwipes(
    val messageId: Long,
    val chatId: Long,
    val sender: SenderType,
    val senderId: Long,
    val index: Int,
    val selectedSwipeIndex: Int,
    val swipes: List<Swipe>
) {
    companion object {
        fun fromMessageAndSwipes(message: Message, swipes: List<Swipe>): MessageWithSwipes {
            return MessageWithSwipes(
                messageId = message.messageId,
                chatId = message.chatId,
                sender = message.sender,
                senderId = message.senderId,
                index = message.index,
                selectedSwipeIndex = message.selectedSwipeIndex,
                swipes = swipes
            )
        }
    }
}
