package ru.kima.intelligentchat.data.persona

import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.kima.intelligentchat.data.image.dataSource.ImageStorage
import ru.kima.intelligentchat.domain.persona.model.Persona

suspend fun PersonaEntity.toPersona(imageStorage: ImageStorage): Persona {
    return Persona(
        id = id,
        name = name,
        description = description,
        bitmap = imageFilePath?.let {
            coroutineScope {
                val job = async(Dispatchers.Unconfined, start = CoroutineStart.LAZY) {
                    val image = imageStorage.getImage(it)
                    BitmapFactory.decodeByteArray(image, 0, image.size)
                }
                job.await()
            }
        },
        personaWordsCount = personaWordsCount,
        charactersWordsCount = charactersWordsCount,
        personaMessages = personaMessages,
        charactersMessages = charactersMessages,
        swipes = swipes
    )
}

fun Persona.toEntity(): PersonaEntity {
    return PersonaEntity(
        id = id,
        name = name,
        description = description,
        imageFilePath = bitmap?.let { getPersonaAvatarFileName(this) },
        personaWordsCount = personaWordsCount,
        charactersWordsCount = charactersWordsCount,
        personaMessages = personaMessages,
        charactersMessages = charactersMessages,
        swipes = swipes
    )
}

fun getPersonaAvatarFileName(persona: Persona) = "persona-${persona.id}.png"