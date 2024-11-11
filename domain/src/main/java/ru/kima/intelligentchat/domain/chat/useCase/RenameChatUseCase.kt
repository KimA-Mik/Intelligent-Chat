package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.ChatNotFoundException
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

class RenameChatUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(chatId: Long, newTitle: String) {
        val chat = try {
            chatRepository.subscribeToChat(chatId).first()
        } catch (_: ChatNotFoundException) {
            return
        }

        val updated = chat.copy(title = newTitle)
        chatRepository.updateChat(updated)
    }
}