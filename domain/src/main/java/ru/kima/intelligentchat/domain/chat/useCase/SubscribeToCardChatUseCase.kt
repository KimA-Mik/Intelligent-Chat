package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.chat.ChatNotFoundException
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

class SubscribeToCardChatUseCase(
    private val chatRepository: ChatRepository,
    private val getCard: GetCardUseCase,
    private val createAndSelectChat: CreateAndSelectChatUseCase,
) {
    operator fun invoke(cardId: Long): Flow<Result> = chatRepository
        .subscribeToCardChat(cardId)
        .map<FullChat, Result> { Result.Success(it) }
        .catch {
            when (it) {
                is ChatNotFoundException -> {
                    val card = getCard(cardId).first()
                    createAndSelectChat(card = card, title = card.name)
                    emitAll(invoke(cardId))
                }

                else -> emit(Result.UnknownError)
            }
        }.flowOn(Dispatchers.IO)

    sealed interface Result {
        data class Success(val fullChat: FullChat) : Result
        data object UnknownError : Result
    }
}