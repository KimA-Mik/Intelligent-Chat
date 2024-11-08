package ru.kima.intelligentchat.domain.chat.useCase

import ru.kima.intelligentchat.core.common.ICResult
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository


class CreateAndSelectChatUseCase(
    private val updateCard: UpdateCardUseCase,
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(card: CharacterCard, title: String): ICResult<Long, Throwable> {
        return try {
            val chat = Chat(chatId = 0, title = title, cardId = card.id, selectedGreeting = 0)
            val id = chatRepository.insertChat(chat)
            val newCard = card.copy(selectedChat = id)
            updateCard(newCard)
            ICResult.Success(id)
        } catch (e: Exception) {
            ICResult.Error(e)
        }
    }
}