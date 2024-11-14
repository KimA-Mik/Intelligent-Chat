package ru.kima.intelligentchat.domain.common.useCase

import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class CleanUpUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke() {
        val markedMessages = messageRepository.getMarkedMessages()
        messageRepository.deleteMessages(markedMessages)
    }
}