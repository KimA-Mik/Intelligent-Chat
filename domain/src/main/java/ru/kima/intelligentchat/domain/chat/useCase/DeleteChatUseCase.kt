package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.core.common.valueOr
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

class DeleteChatUseCase(
    private val chatRepository: ChatRepository,
    private val characterCardRepository: CharacterCardRepository,
    private val subscribeToFullChatUseCase: SubscribeToFullChatUseCase,
) {
    suspend operator fun invoke(chatId: Long) {
        val fullChat = subscribeToFullChatUseCase(chatId).first().valueOr {
            return
        }

        val card = characterCardRepository.getCharacterCard(fullChat.cardId).first()
        if (card.selectedChat != chatId) {
            chatRepository.deleteChat(fullChat)
        }
    }
}