package ru.kima.intelligentchat.domain.chat.useCase.inChat

import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class RestoreSwipeUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(messageId: Long, swipeId: Long) {
        val message = messageRepository.getFullMessage(messageId) ?: return

        var realSwipeIndex = 0
        var listIndex = -1
        for (i in message.swipes.indices) {
            if (message.swipes[i].swipeId == swipeId) {
                listIndex = i
                break
            }

            if (message.swipes[i].deleted) {
                continue
            }

            realSwipeIndex += 1
        }

        if (listIndex < 0) return

        val swipes = message.swipes.toMutableList()
        swipes[listIndex] = swipes[listIndex].copy(deleted = false)

        val updatedMessage = message.copy(
            selectedSwipeIndex = realSwipeIndex,
            swipes = swipes
        )
        messageRepository.updateMessageWithSwipes(updatedMessage)
    }
}