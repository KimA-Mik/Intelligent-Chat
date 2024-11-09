package ru.kima.intelligentchat.presentation.chat.chatScreen

import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayChat
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ImmutableMessagingIndicator


sealed interface ChatScreenState {
    data class ChatState(
        val info: ChatInfo = ChatInfo(),
        val inputMessageBuffer: String = String(),
        val editMessageBuffer: String = String(),
        val editMessageId: Long = 0L,
        val status: ImmutableMessagingIndicator = ImmutableMessagingIndicator.None,
    ) : ChatScreenState {
        data class ChatInfo(
            val characterCard: DisplayCard = DisplayCard(),
            val fullChat: DisplayChat = DisplayChat()
        )
    }

    data object ErrorState : ChatScreenState
}

