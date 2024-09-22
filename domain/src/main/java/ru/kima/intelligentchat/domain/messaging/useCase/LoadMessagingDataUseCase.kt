package ru.kima.intelligentchat.domain.messaging.useCase

import kotlinx.coroutines.flow.last
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToFullChatUseCase
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.model.PersonaImage
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.LoadPersonaImageUseCase

class LoadMessagingDataUseCase(
    private val subscribeToFullChat: SubscribeToFullChatUseCase,
    private val getCardUseCase: GetCardUseCase,
    private val getPersona: GetPersonaUseCase,
    private val loadPersonaImageUseCase: LoadPersonaImageUseCase
) {
    suspend operator fun invoke(chatId: Long): Result {
        val fullChatResult = subscribeToFullChat(chatId).last()
        val fullChat = if (fullChatResult is SubscribeToFullChatUseCase.Result.Success) {
            fullChatResult.fullChat
        } else {
            return Result.NoChat
        }

        val lastMessage = fullChat.messages.lastOrNull() ?: return Result.EmptyChat
        val lastMessageSenderId = lastMessage.senderId
        val lastMessageSenderType = lastMessage.sender

        val lastSender = when (lastMessageSenderType) {
            SenderType.Character -> loadCharacter(lastMessageSenderId)
            SenderType.Persona -> loadPersona(lastMessageSenderId)
        }

        return Result.Success(fullChat, lastSender)
    }

    private suspend fun loadCharacter(id: Long): LastSender {
        val card = getCardUseCase(id).last()

        return LastSender.CharacterSender(card)
    }

    private suspend fun loadPersona(id: Long): LastSender {
        val persona = getPersona(id)
        val image = loadPersonaImageUseCase(id)

        return LastSender.PersonaSender(persona, image)
    }

    sealed interface Result {
        data object NoChat : Result
        data object EmptyChat : Result
        data class Success(val fullChat: FullChat, val sender: LastSender) : Result
    }

    sealed interface LastSender {
        data class CharacterSender(val card: CharacterCard) : LastSender
        data class PersonaSender(val persona: Persona, val image: PersonaImage) : LastSender
    }
}