package ru.kima.intelligentchat.domain.messaging.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.useCase.InitializeChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.CreateMessageUseCase
import ru.kima.intelligentchat.domain.messaging.repositoty.MessagingRepository
import ru.kima.intelligentchat.domain.persona.useCase.SelectedPersonaUseCase

class SendMessageUseCase(
    private val createMessage: CreateMessageUseCase,
    private val initializeChat: InitializeChatUseCase,
    private val messagingRepository: MessagingRepository,
    private val getSelectedPersona: SelectedPersonaUseCase,
) {
    //TODO: Handle actual sending of a message
    suspend operator fun invoke(
        chatId: Long,
        personaId: Long,
        text: String
    ) {
        if (text.isBlank()) return
        val selectedPersona = getSelectedPersona().first()
        initializeChat(chatId, selectedPersona)

        createMessage(chatId, SenderType.Persona, personaId, text)
        messagingRepository.initiateGeneration(
            chatId = chatId,
            personaId = selectedPersona.id,
            senderType = SenderType.Character
        )
    }
}