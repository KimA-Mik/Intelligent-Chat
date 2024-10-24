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
    suspend operator fun invoke(chatId: Long, personaId: Long, senderType: SenderType): Result {
        val fullChatResult = subscribeToFullChat(chatId).last()
        val fullChat = if (fullChatResult is SubscribeToFullChatUseCase.Result.Success) {
            fullChatResult.fullChat
        } else {
            return Result.NoChat
        }

        val sender = when (senderType) {
            SenderType.Character -> loadCharacter(fullChat.cardId)
            SenderType.Persona -> loadPersona(personaId)
        }

        if (sender is Sender.PersonaSender) {
            return Result.Success(fullChat, sender, sender.persona, sender.image)
        }

        val persona = getPersona(personaId)
        val image = loadPersonaImageUseCase(personaId)
        return Result.Success(fullChat, sender, persona, image)
    }

    private suspend fun loadCharacter(id: Long): Sender {
        val card = getCardUseCase(id).last()

        return Sender.CharacterSender(card)
    }

    private suspend fun loadPersona(id: Long): Sender {
        val persona = getPersona(id)
        val image = loadPersonaImageUseCase(id)

        return Sender.PersonaSender(persona, image)
    }

    sealed interface Result {
        data object NoChat : Result
        data class Success(
            val fullChat: FullChat,
            val sender: Sender,
            val persona: Persona,
            val personaImage: PersonaImage
        ) : Result
    }

    sealed interface Sender {
        data class CharacterSender(val card: CharacterCard) : Sender
        data class PersonaSender(val persona: Persona, val image: PersonaImage) : Sender
    }
}