package ru.kima.intelligentchat.domain.chat.useCase.inChat

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToFullChatUseCase
import ru.kima.intelligentchat.domain.common.errors.Formatter
import ru.kima.intelligentchat.domain.common.valueOr

class BranchChatFromMessageUseCase(
    private val subscribeToFullChatUseCase: SubscribeToFullChatUseCase,
    private val characterCardRepository: CharacterCardRepository,
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(chatId: Long, messageId: Long) {
        if (chatId == 0L || messageId == 0L) return
        val chat = subscribeToFullChatUseCase(chatId).first().valueOr {
            return
        }

        val card = characterCardRepository.getCharacterCard(chat.cardId).first()
        val messageIndex = chat.messages.indexOfFirst { it.messageId == messageId }
        if (messageIndex < 0) return
        val messagesForCoping = if (messageIndex == chat.messages.lastIndex) {
            chat.messages
        } else {
            chat.messages.subList(0, messageIndex + 1)
        }

        val newTitle = "Branch ${card.name} ${Formatter.defaultFormat(System.currentTimeMillis())}"
        val newChat = chat.copy(
            title = newTitle,
            messages = messagesForCoping
        )

        val updatedChatId = chatRepository.copyChat(newChat)
        val newCard = card.copy(selectedChat = updatedChatId)
        characterCardRepository.updateCharacterCard(newCard)
    }
}