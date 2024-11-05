package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.core.common.valueOr
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.chat.ChatNotFoundException
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class SubscribeToCardChatUseCase(
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val getCard: GetCardUseCase,
    private val createAndSelectChat: CreateAndSelectChatUseCase,
    private val characterCardRepository: CharacterCardRepository
) {
    operator fun invoke(cardId: Long): Flow<Result> = flow {
        val card = characterCardRepository.getCharacterCard(cardId).first()
        val chatId = if (card.selectedChat == 0L) {
            createAndSelectChat(card, card.name).valueOr {
                return@flow
            }
        } else {
            card.selectedChat
        }

        combine(
            chatRepository.subscribeToChat(chatId),
            messageRepository.subscribeToChatMessagesWithSwipes(chatId)
        ) { chat, messages ->
            Result.Success(
                FullChat(
                    chatId = chat.chatId,
                    title = chat.title,
                    cardId = chat.cardId,
                    selectedGreeting = chat.selectedGreeting,
                    messages = messages
                )
            )
        }.catch {
            when (it) {
                is ChatNotFoundException -> {
                    val c = getCard(cardId).first()
                    createAndSelectChat(card = c, title = c.name)
                    emitAll(invoke(cardId))
                }

                else -> emit(Result.UnknownError)
            }
        }.collect {
            emit(it)
        }
    }

    sealed interface Result {
        data class Success(val fullChat: FullChat) : Result
        data object UnknownError : Result
    }
}