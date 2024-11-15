package ru.kima.intelligentchat.domain.chat.useCase.inChat

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class RestoreMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(messageId: Long) {
        val message = messageRepository.getMessage(messageId) ?: return
        val otherMessages = messageRepository.subscribeToChatMessages(message.chatId).first()

        val updated = mutableListOf(message.copy(deleted = false))
        for (i in message.index - 1..otherMessages.lastIndex) {
            updated.add(otherMessages[i].copy(index = i + 2))
        }

        messageRepository.updateMessages(updated)
    }
}