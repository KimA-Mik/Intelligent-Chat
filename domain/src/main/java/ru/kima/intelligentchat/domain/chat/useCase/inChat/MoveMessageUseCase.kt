package ru.kima.intelligentchat.domain.chat.useCase.inChat

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class MoveMessageUseCase(
    private val messageRepository: MessageRepository
) {
    enum class Direction {
        Up, Down
    }

    suspend operator fun invoke(chatId: Long, messageId: Long, direction: Direction) {
        val messages = messageRepository.subscribeToChatMessages(chatId).first()
        if (messages.size < 2) return

        val index = messages.indexOfFirst { it.messageId == messageId }
        if (index < 0) return

        val expectedIndex = when (direction) {
            Direction.Up -> index - 1
            Direction.Down -> index + 1
        }

        if (expectedIndex < 0 || expectedIndex > messages.lastIndex) return
        val resultList = listOf(
            messages[index].copy(index = expectedIndex),
            messages[expectedIndex].copy(index = index)
        )
        messageRepository.updateMessages(resultList)
    }
}