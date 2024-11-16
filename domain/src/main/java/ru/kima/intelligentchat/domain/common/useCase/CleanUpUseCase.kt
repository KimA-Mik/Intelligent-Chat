package ru.kima.intelligentchat.domain.common.useCase

import ru.kima.intelligentchat.domain.chat.repository.MessageRepository
import ru.kima.intelligentchat.domain.chat.repository.SwipeRepository

class CleanUpUseCase(
    private val messageRepository: MessageRepository,
    private val swipeRepository: SwipeRepository
) {
    suspend operator fun invoke() {
        val markedMessages = messageRepository.getMarkedMessages()
        if (markedMessages.isNotEmpty()) {
            messageRepository.deleteMessages(markedMessages)
        }

        val markedSwipes = swipeRepository.getMarkedSwipes()
        if (markedSwipes.isNotEmpty()) {
            swipeRepository.deleteSwipes(markedSwipes)
        }
    }
}