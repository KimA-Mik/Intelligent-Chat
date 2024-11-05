package ru.kima.intelligentchat.presentation.chat.chatScreen

import ru.kima.intelligentchat.domain.messaging.model.MessagingIndicator
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayChat


sealed interface ChatScreenState {
    data class ChatState(
        val info: ChatInfo = ChatInfo(),
        val inputMessageBuffer: String = String(),
        val editMessageBuffer: String = String(),
        val status: MessagingIndicator = MessagingIndicator.None,
    ) : ChatScreenState {
        data class ChatInfo(
            val characterCard: DisplayCard = DisplayCard(),
            val fullChat: DisplayChat = DisplayChat()
        )
    }

    data object ErrorState : ChatScreenState
}

