package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.ChatWithMessages
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

class SubscribeToCardChatsUseCase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(cardId: Long): Flow<List<ChatWithMessages>> {
        return chatRepository.subscribeToCardChats(cardId)
    }
}