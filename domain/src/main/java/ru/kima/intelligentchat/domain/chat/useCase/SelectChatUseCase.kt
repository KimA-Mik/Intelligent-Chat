package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class SelectChatUseCase(
    private val cardRepository: CharacterCardRepository,

    ) {
    suspend operator fun invoke(chatId: Long, cardId: Long) {
        val card = cardRepository.getCharacterCard(cardId).first().copy(selectedChat = chatId)
        cardRepository.updateCharacterCard(card)
    }
}