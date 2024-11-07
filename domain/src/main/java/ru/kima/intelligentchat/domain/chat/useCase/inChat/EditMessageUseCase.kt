package ru.kima.intelligentchat.domain.chat.useCase.inChat

import ru.kima.intelligentchat.domain.chat.repository.MessageRepository
import ru.kima.intelligentchat.domain.chat.repository.SwipeRepository

class EditMessageUseCase(
    private val messageRepository: MessageRepository,
    private val swipeRepository: SwipeRepository,
) {
    suspend operator fun invoke(messageId: Long, text: String) {
        val fullMessage = messageRepository.getFullMessage(messageId) ?: return
        val swipe = fullMessage.swipes.getOrElse(fullMessage.selectedSwipeIndex) { return }

        val newSwipe = swipe.copy(text = text)
        swipeRepository.updateSwipe(newSwipe)
    }
}