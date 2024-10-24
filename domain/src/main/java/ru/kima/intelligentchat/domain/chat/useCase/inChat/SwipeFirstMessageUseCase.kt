package ru.kima.intelligentchat.domain.chat.useCase.inChat

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.chat.model.SwipeDirection
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

class SwipeFirstMessageUseCase(
    private val getCard: GetCardUseCase,
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(cardId: Long, chatId: Long, direction: SwipeDirection) {
        val card = try {
            getCard(cardId).first()
        } catch (e: NullPointerException) {
            return
        }

        val chat = chatRepository.subscribeToChat(chatId).first()

        val totalGreetings = card.alternateGreetings.size + 1
        if (totalGreetings == 1) {
            return
        }

        val add = when (direction) {
            SwipeDirection.Left -> -1
            SwipeDirection.Right -> 1
        }

        var newSwipe = chat.selectedGreeting + add
        if (newSwipe < 0) newSwipe = totalGreetings - 1
        if (newSwipe >= totalGreetings) newSwipe = 0

        chatRepository.updateChat(chat.copy(selectedGreeting = newSwipe))
    }
}