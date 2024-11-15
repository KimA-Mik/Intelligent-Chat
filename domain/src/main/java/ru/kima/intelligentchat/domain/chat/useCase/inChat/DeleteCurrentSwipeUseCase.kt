package ru.kima.intelligentchat.domain.chat.useCase.inChat

import ru.kima.intelligentchat.core.common.ICResult
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class DeleteCurrentSwipeUseCase(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(messageId: Long): ICResult<Success, Error> {
        val message = messageRepository.getFullMessage(messageId)
            ?: return ICResult.Error(Error.IncorrectId)

        if (message.swipes.size < 2) return ICResult.Error(Error.FewSwipes)

        val swipe = message.swipes
            .getOrNull(message.selectedSwipeIndex)
            ?.copy(deleted = true) ?: return ICResult.Error(Error.IncorrectSelectedSwipeId)

        val updatedSwipes = message.swipes.toMutableList()
        updatedSwipes[message.selectedSwipeIndex] = swipe

        val updatedMessage = message.copy(
            selectedSwipeIndex = (message.selectedSwipeIndex - 1).coerceIn(0 until message.swipes.size - 1),
            swipes = updatedSwipes
        )

        messageRepository.updateMessageWithSwipes(updatedMessage)
        return ICResult.Success(Success(message.selectedSwipeIndex))
    }

    sealed interface Error {
        data object IncorrectId : Error
        data object FewSwipes : Error
        data object IncorrectSelectedSwipeId : Error
    }

    data class Success(val deletedIndex: Int)
}