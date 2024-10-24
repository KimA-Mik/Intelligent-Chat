package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import ru.kima.intelligentchat.domain.chat.ChatNotFoundException
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

//TODO: clear up unused use cases
class SubscribeToFullChatUseCase(
    private val chatRepository: ChatRepository,
    private val chatMessages: SubscribeToChatMessagesWithSwipesUseCase,
) {
    operator fun invoke(chatId: Long): Flow<Result> =
        combine<Chat, List<MessageWithSwipes>, Result>(
            chatRepository.subscribeToChat(chatId),
            chatMessages(chatId)
        ) { chat, messages ->
            Result.Success(FullChat.fromChatAndMessages(chat, messages))
        }.catch {
            when (it) {
                is ChatNotFoundException -> emit(Result.ChatNotFound)
                else -> emit(Result.UnknownError)
            }
        }

    sealed interface Result {
        data class Success(val fullChat: FullChat) : Result
        data object ChatNotFound : Result
        data object UnknownError : Result
    }
}