package ru.kima.intelligentchat.domain.messaging.useCase

import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.useCase.InitializeChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.CreateMessageUseCase

class SendMessageUseCase(
    private val createMessage: CreateMessageUseCase,
    private val initializeChat: InitializeChatUseCase
) {
    //TODO: Handle actual sending of a message
    suspend operator fun invoke(
        chatId: Long,
        sender: SenderType,
        senderId: Long,
        text: String
    ) {
        if (text.isBlank()) return
        initializeChat(chatId)

        createMessage(chatId, sender, senderId, text)
    }
}