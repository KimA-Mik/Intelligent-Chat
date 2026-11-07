package ru.kima.intelligentchat.domain.debug.useCase

import kotlinx.coroutines.flow.firstOrNull
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class NormalizeChatUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(chatId: Long): Result<Unit> {
        val messages = messageRepository.subscribeToChatMessages(chatId).firstOrNull()
            ?: return Result.failure(IllegalStateException("No messages found for chatId: $chatId"))
        val normalized = messages.mapIndexed { index, message ->
            message.copy(
                sender = if (index % 2 == 0) SenderType.Character else SenderType.Persona,
            )
        }

        messageRepository.updateMessages(normalized)
        return Result.success(Unit)
    }
}