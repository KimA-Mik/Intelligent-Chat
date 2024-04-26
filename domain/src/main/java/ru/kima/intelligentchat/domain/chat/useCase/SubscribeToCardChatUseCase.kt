package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.chat.ChatNotFoundException
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

class SubscribeToCardChatUseCase(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(cardId: Long): Flow<Result> = chatRepository
        .subscribeToCardChat(cardId)
        .map<FullChat, Result> { Result.Success(it) }
        .catch {
            val result = when (it) {
                is ChatNotFoundException -> Result.NotFound
                else -> Result.UnknownError
            }
            emit(result)
        }.flowOn(Dispatchers.IO)

    sealed interface Result {
        data class Success(val fullChat: FullChat) : Result
        data object NotFound : Result
        data object UnknownError : Result
    }
}