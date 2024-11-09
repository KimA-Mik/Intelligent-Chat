package ru.kima.intelligentchat.domain.chat.useCase.inChat

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.model.Message
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.model.SwipeDirection
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository
import ru.kima.intelligentchat.domain.messaging.repositoty.MessagingRepository
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase

class SwipeMessageUseCase(
    private val messageRepository: MessageRepository,
    private val messagingRepository: MessagingRepository,
    private val preferences: GetPreferencesUseCase
) {
    suspend operator fun invoke(messageId: Long, direction: SwipeDirection): Result {
        val message = messageRepository.getFullMessage(messageId)
            ?: return Result.NoMessage

        val selectedPersona = preferences().first().selectedPersonaId
        var newSwipeIndex = message.selectedSwipeIndex + when (direction) {
            SwipeDirection.Left -> -1
            SwipeDirection.Right -> 1
        }

        if (newSwipeIndex < 0) newSwipeIndex = message.swipes.lastIndex
        if (newSwipeIndex >= message.swipes.size) {
            if (!messagingRepository.isGenerationAvailable()) return Result.Success
            newSwipeIndex = message.swipes.size
            messagingRepository.initiateSwipeGeneration(
                chatId = message.chatId,
                personaId = selectedPersona,
                messageId = messageId,
                senderType = SenderType.Character
            )
        }

        val updatedMessage = Message(
            messageId = message.messageId,
            chatId = message.chatId,
            sender = message.sender,
            senderId = message.senderId,
            index = message.index,
            selectedSwipeIndex = newSwipeIndex,
            deleted = message.deleted
        )
        messageRepository.updateMessage(updatedMessage)

        return Result.Success
    }

    sealed interface Result {
        data object Success : Result
        data object NoMessage : Result
    }
}