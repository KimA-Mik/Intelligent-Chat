package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

class DeleteChatUseCase(
    private val chatRepository: ChatRepository,
    private val subscribeToFullChatUseCase: SubscribeToFullChatUseCase,
) {
    suspend operator fun invoke(chatId: Long) {
        val fullChatResult = subscribeToFullChatUseCase(chatId).first()
        val fullChat = when (fullChatResult) {
            is SubscribeToFullChatUseCase.Result.Success -> fullChatResult.fullChat
            else -> return
        }

        chatRepository.deleteChat(fullChat)
    }
}