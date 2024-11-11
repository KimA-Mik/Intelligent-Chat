package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository
import java.text.SimpleDateFormat

class CreateChatUseCase(
    private val chatRepository: ChatRepository,
    private val cardRepository: CharacterCardRepository,
) {
    suspend operator fun invoke(cardId: Long) {
        val card = cardRepository.getCharacterCard(cardId).first()
        val currentTime = System.currentTimeMillis()
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val formatted = formatter.format(currentTime)
        val chat = Chat(
            chatId = 0L,
            title = "${card.name} $formatted",
            cardId = cardId,
            selectedGreeting = 0
        )
        chatRepository.insertChat(chat)
    }

}