package ru.kima.intelligentchat.domain.chat.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.useCase.inChat.CreateMessageUseCase
import ru.kima.intelligentchat.domain.common.valueOr
import ru.kima.intelligentchat.domain.messaging.util.inlineCardName
import ru.kima.intelligentchat.domain.messaging.util.inlinePersonaName
import ru.kima.intelligentchat.domain.persona.model.Persona

class InitializeChatUseCase(
    private val subscribeToChatMessages: SubscribeToFullChatUseCase,
    private val getCard: GetCardUseCase,
    private val createMessage: CreateMessageUseCase
) {
    suspend operator fun invoke(chatId: Long, persona: Persona) {
        val chat = subscribeToChatMessages(chatId).first().valueOr {
            return
        }

        if (chat.messages.isNotEmpty()) return

        //TODO: Handle errors
        val card = getCard(chat.cardId).first()
        val firstMessage = if (chat.selectedGreeting == 0) {
            card.firstMes
        } else {
            card.alternateGreetings.getOrNull(chat.selectedGreeting - 1)?.body ?: card.firstMes
        }

        val preparedMessage = firstMessage
            .inlineCardName(card.name)
            .inlinePersonaName(persona.name)

        createMessage(
            chatId = chatId,
            sender = SenderType.Character,
            senderId = card.id,
            text = preparedMessage
        )
    }
}