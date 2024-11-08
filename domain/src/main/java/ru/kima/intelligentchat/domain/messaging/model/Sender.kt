package ru.kima.intelligentchat.domain.messaging.model

import android.graphics.Bitmap
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.model.PersonaImage

sealed interface Sender {
    data class CharacterSender(val card: CharacterCard) : Sender
    data class PersonaSender(val persona: Persona, val image: PersonaImage) : Sender

    val name: String
        get() = when (this) {
            is CharacterSender -> card.name
            is PersonaSender -> persona.name
        }

    val photo: Bitmap?
        get() = when (this) {
            is CharacterSender -> card.photoBytes
            is PersonaSender -> image.bitmap
        }

    val id: Long
        get() = when (this) {
            is CharacterSender -> card.id
            is PersonaSender -> persona.id
        }

    val type: SenderType
        get() = when (this) {
            is CharacterSender -> SenderType.Character
            is PersonaSender -> SenderType.Persona
        }
}
