package ru.kima.intelligentchat.domain.messaging.generation.savingResult

import ru.kima.intelligentchat.domain.chat.repository.SwipeRepository
import ru.kima.intelligentchat.domain.messaging.model.Sender

class SwipeSavingStrategy(
    private val messageId: Long,
    private val swipeRepository: SwipeRepository,
) : SavingStrategy {
    override suspend fun save(text: String, sender: Sender) {
        swipeRepository.createSwipe(messageId, text)
    }
}