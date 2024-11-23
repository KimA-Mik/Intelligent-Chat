package ru.kima.intelligentchat.data.card.mappers

import ru.kima.intelligentchat.data.card.entities.CardEntity
import ru.kima.intelligentchat.data.card.entities.CharacterEntity
import ru.kima.intelligentchat.domain.card.model.CharacterCard

fun CardEntity.toCharacterCard(
    tags: List<String> = emptyList()
): CharacterCard {
    return CharacterCard(
        id = character.id,
        photoName = character.photoFilePath,
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
        selectedChat = character.selectedChat,
        selectedGreeting = character.selectedGreeting
    )
}

fun CharacterCard.toEntity(): CardEntity {
    return CardEntity(character = CharacterEntity(
        id = id,
        photoFilePath = photoName,
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
        selectedChat = selectedChat,
        selectedGreeting = selectedGreeting
    ), altGreetings = alternateGreetings.map { it.toEntity(id) })
}