package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository
import ru.kima.intelligentchat.domain.chat.repository.SwipeRepository

class SubscribeToChatMessagesWithSwipesUseCase(
    private val messageRepository: MessageRepository,
    private val swipeRepository: SwipeRepository
) {
    operator fun invoke(chatId: Long): Flow<List<MessageWithSwipes>> = combine(
        messageRepository.subscribeToChatMessages(chatId),
        swipeRepository.subscribeSwipesForChart(chatId).map { swipes ->
            swipes
                .filter { !it.deleted }
                .groupBy { it.messageId }
        }
    ) { messages, swipes ->
        messages.map { message ->
            MessageWithSwipes.fromMessageAndSwipes(
                message = message,
                swipes = swipes.getOrElse(message.messageId) { emptyList() }
            )
        }
    }.flowOn(Dispatchers.IO)
}