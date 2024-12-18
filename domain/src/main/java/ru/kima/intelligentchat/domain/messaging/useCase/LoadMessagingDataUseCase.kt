package ru.kima.intelligentchat.domain.messaging.useCase

import android.graphics.BitmapFactory
import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToFullChatUseCase
import ru.kima.intelligentchat.domain.common.valueOr
import ru.kima.intelligentchat.domain.images.ImageStorage
import ru.kima.intelligentchat.domain.messaging.model.Sender
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.model.PersonaImage
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.LoadPersonaImageUseCase

class LoadMessagingDataUseCase(
    private val subscribeToFullChat: SubscribeToFullChatUseCase,
    private val getCard: GetCardUseCase,
    private val imageStorage: ImageStorage,
    private val getPersona: GetPersonaUseCase,
    private val loadPersonaImage: LoadPersonaImageUseCase
) {
    suspend operator fun invoke(chatId: Long, personaId: Long, senderType: SenderType): Result {
        val fullChat = subscribeToFullChat(chatId).first().valueOr {
            return Result.NoChat
        }

        val sender = when (senderType) {
            SenderType.Character -> loadCharacter(fullChat.cardId)
            SenderType.Persona -> loadPersona(personaId)
        }

        return when (sender) {
            is Sender.CharacterSender -> Result.Success(
                card = sender.card,
                fullChat = fullChat,
                sender = sender,
                persona = getPersona(personaId),
                personaImage = loadPersonaImage(personaId)
            )

            is Sender.PersonaSender -> Result.Success(
                card = getCard(chatId).first(),
                fullChat = fullChat,
                sender = sender,
                persona = sender.persona,
                personaImage = sender.image
            )
        }
    }

    private suspend fun loadCharacter(id: Long): Sender {
        val card = getCard(id).first()
        val image = card.photoName?.let {
            val data = imageStorage.getImage(it)
            BitmapFactory.decodeByteArray(data, 0, data.size)
        }

        return Sender.CharacterSender(card, image)
    }

    private suspend fun loadPersona(id: Long): Sender {
        val persona = getPersona(id)
        val image = loadPersonaImage(id)

        return Sender.PersonaSender(persona, image)
    }

    sealed interface Result {
        data object NoChat : Result
        data class Success(
            val card: CharacterCard,
            val fullChat: FullChat,
            val sender: Sender,
            val persona: Persona,
            val personaImage: PersonaImage
        ) : Result
    }
}