package ru.kima.intelligentchat.domain.chat.useCase

import android.util.Log
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

private const val TAG = "CreateAndSelectChatUseCase"

class CreateAndSelectChatUseCase(
    private val updateCard: UpdateCardUseCase,
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(card: CharacterCard, title: String) {
        try {
            val chat = Chat(chatId = 0, title = title, cardId = card.id, selectedGreeting = 0)
            val id = chatRepository.insertChat(chat)
            val newCard = card.copy(selectedChat = id)
            updateCard(newCard)
        } catch (e: Exception) {
            e.message?.let {
                Log.e(TAG, it)
            }
        }
    }
}