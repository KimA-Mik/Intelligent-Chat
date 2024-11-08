package ru.kima.intelligentchat.domain.messaging.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository
import ru.kima.intelligentchat.domain.chat.useCase.InitializeChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.CreateMessageUseCase
import ru.kima.intelligentchat.domain.messaging.repositoty.MessagingRepository
import ru.kima.intelligentchat.domain.persona.useCase.SelectedPersonaUseCase

class SendMessageUseCase(
    private val createMessage: CreateMessageUseCase,
    private val initializeChat: InitializeChatUseCase,
    private val messagingRepository: MessagingRepository,
    private val getSelectedPersona: SelectedPersonaUseCase,
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(
        chatId: Long,
        personaId: Long,
        text: String
    ) {
        val selectedPersona = getSelectedPersona().first()
        initializeChat(chatId, selectedPersona)

        if (text.isNotBlank()) {
            createMessage(chatId, SenderType.Persona, personaId, text.trim())
        } else {
            val messages = messageRepository.subscribeToChatMessages(chatId).first()
            if (messages.isEmpty()) return
            //That's job for impersonation
            if (messages.last().sender == SenderType.Character) return
        }

        messagingRepository.initiateGeneration(
            chatId = chatId,
            personaId = selectedPersona.id,
            senderType = SenderType.Character
        )
    }
}