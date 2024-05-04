package ru.kima.intelligentchat.domain.chat.useCase.inChat

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.domain.chat.model.SwipeDirection

class SwipeFirstMessageUseCase(
    private val getCard: GetCardUseCase,
    private val updateCard: UpdateCardUseCase
) {
    suspend operator fun invoke(cardId: Long, direction: SwipeDirection) {
        val card = try {
            getCard(cardId).first()
        } catch (e: NullPointerException) {
            return
        }

        val totalGreetings = card.alternateGreetings.size + 1
        if (totalGreetings == 1) {
            return
        }

        val add = when (direction) {
            SwipeDirection.Left -> -1
            SwipeDirection.Right -> 1
        }

        var newSwipe = card.selectedGreeting + add
        if (newSwipe < 1) newSwipe = totalGreetings
        if (newSwipe > totalGreetings) newSwipe = 1

        updateCard(card.copy(selectedGreeting = newSwipe))
    }
}