package ru.kima.intelligentchat.domain.chat.useCase.inChat

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository
import ru.kima.intelligentchat.domain.chat.repository.SwipeRepository

class CreateMessageUseCase(
    private val messageRepository: MessageRepository,
    private val swipeRepository: SwipeRepository
) {
    suspend operator fun invoke(
        chatId: Long, sender: SenderType,
        senderId: Long, text: String
    ): Long {
        val messages = messageRepository.subscribeToChatMessages(chatId).first()
        val messageIndex = messages.lastOrNull()?.index?.plus(1) ?: 1
        val newMessageId = messageRepository.createMessage(
            chatId = chatId,
            sender = sender,
            senderId = senderId,
            index = messageIndex,
            selectedSwipeIndex = 0
        )
        swipeRepository.createSwipe(messageId = newMessageId, text = text)
        return newMessageId
    }
}