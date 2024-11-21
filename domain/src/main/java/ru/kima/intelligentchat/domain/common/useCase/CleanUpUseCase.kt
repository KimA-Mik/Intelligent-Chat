package ru.kima.intelligentchat.domain.common.useCase

import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository
import ru.kima.intelligentchat.domain.chat.repository.SwipeRepository

class CleanUpUseCase(
    private val cardRepository: CharacterCardRepository,
    private val messageRepository: MessageRepository,
    private val swipeRepository: SwipeRepository,
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke() {
        val markedMessages = messageRepository.getMarkedMessages()
        if (markedMessages.isNotEmpty()) {
            messageRepository.deleteMessages(markedMessages)
        }

        val markedSwipes = swipeRepository.getMarkedSwipes()
        if (markedSwipes.isNotEmpty()) {
            swipeRepository.deleteSwipes(markedSwipes)
        }

        cleanUpCards()
    }

    private suspend fun cleanUpCards() {
        val markedCards = cardRepository.getMarkedCards()
        if (markedCards.isEmpty()) return
        cardRepository.deleteCards(markedCards)

        val ids = markedCards.map { it.id }
        val chats = chatRepository.getChatsForCardIds(ids)
        if (chats.isEmpty()) return
        chatRepository.deleteChats(chats)
    }
}