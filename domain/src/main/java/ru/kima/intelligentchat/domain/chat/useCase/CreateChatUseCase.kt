package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository
import ru.kima.intelligentchat.domain.common.errors.Formatter

class CreateChatUseCase(
    private val chatRepository: ChatRepository,
    private val cardRepository: CharacterCardRepository,
) {
    suspend operator fun invoke(cardId: Long) {
        val card = cardRepository.getCharacterCard(cardId).first()
        val formatted = Formatter.defaultFormat(System.currentTimeMillis())
        val chat = Chat(
            chatId = 0L,
            title = "${card.name} $formatted",
            cardId = cardId,
            selectedGreeting = 0
        )
        chatRepository.insertChat(chat)
    }

}