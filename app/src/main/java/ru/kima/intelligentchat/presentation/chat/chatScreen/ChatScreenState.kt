package ru.kima.intelligentchat.presentation.chat.chatScreen

import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayChat
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ImmutableMessagingIndicator

data class ChatState(
    val info: ChatInfo = ChatInfo(),
    val inputMessageBuffer: String = "",
    val editMessageBuffer: String = "",
    val editMessageId: Long = 0L,
    val status: ImmutableMessagingIndicator = ImmutableMessagingIndicator.None,
    val openUriRequestDialog: Boolean = false,
    val uriToOpen: String = "",
) {
    data class ChatInfo(
        val characterCard: DisplayCard = DisplayCard(),
        val fullChat: DisplayChat = DisplayChat()
    )
}


