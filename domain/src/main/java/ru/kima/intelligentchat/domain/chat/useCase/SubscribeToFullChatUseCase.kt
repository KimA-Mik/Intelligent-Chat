package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import ru.kima.intelligentchat.core.common.ICResult
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
    operator fun invoke(chatId: Long) =
        combine<Chat, List<MessageWithSwipes>, ICResult<FullChat, Error>>(
            chatRepository.subscribeToChat(chatId),
            chatMessages(chatId)
        ) { chat, messages ->
            ICResult.Success(FullChat.fromChatAndMessages(chat, messages))
        }.flowOn(Dispatchers.Default).catch {
            when (it) {
                is ChatNotFoundException -> emit(ICResult.Error(Error.ChatNotFound))
                else -> emit(ICResult.Error(Error.UnknownError))
            }
        }

    sealed interface Error {
        data object ChatNotFound : Error
        data object UnknownError : Error
    }
}