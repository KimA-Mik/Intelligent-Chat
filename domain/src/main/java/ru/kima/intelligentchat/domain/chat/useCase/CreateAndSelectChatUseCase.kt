package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository
import ru.kima.intelligentchat.domain.common.ICResult


class CreateAndSelectChatUseCase(
    private val updateCard: UpdateCardUseCase,
    private val chatRepository: ChatRepository,
    private val cardRepository: CharacterCardRepository
) {
    suspend operator fun invoke(cardId: Long): ICResult<Long, Throwable> {
        return try {
            val card = cardRepository.getCharacterCard(cardId).first()
            val chat = Chat(chatId = 0, title = card.name, cardId = card.id, selectedGreeting = 0)
            val id = chatRepository.insertChat(chat)
            val newCard = card.copy(selectedChat = id)
            updateCard(newCard)
            ICResult.Success(id)
        } catch (e: Exception) {
            ICResult.Error(e)
        }
    }
}