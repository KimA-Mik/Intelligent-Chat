package ru.kima.intelligentchat.domain.messaging.generation.savingResult

import ru.kima.intelligentchat.domain.chat.useCase.inChat.CreateMessageUseCase
import ru.kima.intelligentchat.domain.messaging.model.Sender

class DefaultSavingStrategy(
    private val chatId: Long,
    private val createMessage: CreateMessageUseCase
) : SavingStrategy {
    override suspend fun save(text: String, sender: Sender) {
        createMessage(chatId, sender.type, sender.id, text)
    }
}