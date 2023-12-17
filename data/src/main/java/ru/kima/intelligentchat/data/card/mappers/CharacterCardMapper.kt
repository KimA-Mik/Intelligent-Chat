package ru.kima.intelligentchat.data.card.mappers

import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.kima.intelligentchat.data.card.entities.CardEntity
import ru.kima.intelligentchat.data.card.entities.CharacterEntity
import ru.kima.intelligentchat.data.card.util.getCardPhotoName
import ru.kima.intelligentchat.data.image.dataSource.ImageStorage
import ru.kima.intelligentchat.domain.card.model.CharacterCard

suspend fun CardEntity.toCharacterCard(
    imageStorage: ImageStorage, tags: List<String> = emptyList()
): CharacterCard {
    return CharacterCard(
        character.id,
        character.photoFilePath?.let {
            coroutineScope {
                val job = async(Dispatchers.Unconfined, start = CoroutineStart.LAZY) {
                    val image = imageStorage.getImage(it)
                    BitmapFactory.decodeByteArray(image, 0, image.size)
                }
                job.await()
            }
        },
        character.name,
        character.description,
        character.personality,
        character.scenario,
        character.firstMes,
        character.mesExample,
        character.creatorNotes,
        character.systemPrompt,
        character.postHistoryInstructions,
        altGreetings.map { it.toAltGreeting() },
        tags,
        character.creator,
        character.characterVersion
    )
}

fun CharacterCard.toEntity(): CardEntity {
    return CardEntity(character = CharacterEntity(
        id = id,
        photoFilePath = photoBytes?.let { getCardPhotoName(id) },
        name = name,
        description = description,
        personality = personality,
        scenario = scenario,
        firstMes = firstMes,
        mesExample = mesExample,
        creatorNotes = creatorNotes,
        systemPrompt = systemPrompt,
        postHistoryInstructions = postHistoryInstructions,
        creator = creator,
        characterVersion = characterVersion
    ), altGreetings = alternateGreetings.map { it.toEntity(id) })
}