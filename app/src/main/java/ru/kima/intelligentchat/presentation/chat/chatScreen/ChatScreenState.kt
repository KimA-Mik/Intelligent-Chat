package ru.kima.intelligentchat.presentation.chat.chatScreen

import ru.kima.intelligentchat.domain.card.model.CharacterCard


sealed interface ChatScreenState {
    data class ChatState(
        val characterCard: CharacterCard = CharacterCard(),
        val inputMessageBuffer: String = String(),
        val messages: List<Long> = emptyList()
    ) : ChatScreenState

    data object ErrorState : ChatScreenState
}

