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
        id = character.id,
        photoBytes = character.photoFilePath?.let {
            coroutineScope {
                val job = async(Dispatchers.Unconfined, start = CoroutineStart.LAZY) {
                    val image = imageStorage.getImage(it)
                    BitmapFactory.decodeByteArray(image, 0, image.size)
                }
                job.await()
            }
        },
        name = character.name,
        description = character.description,
        personality = character.personality,
        scenario = character.scenario,
        firstMes = character.firstMes,
        mesExample = character.mesExample,
        creatorNotes = character.creatorNotes,
        systemPrompt = character.systemPrompt,
        postHistoryInstructions = character.postHistoryInstructions,
        alternateGreetings = altGreetings.map { it.toAltGreeting() },
        tags = tags,
        creator = character.creator,
        characterVersion = character.characterVersion,
        deleted = character.deleted,
        selectedChat = character.selectedChat
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
        characterVersion = characterVersion,
        deleted = deleted,
    ), altGreetings = alternateGreetings.map { it.toEntity(id) })
}