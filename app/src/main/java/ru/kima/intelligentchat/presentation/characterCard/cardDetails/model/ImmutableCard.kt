package ru.kima.intelligentchat.presentation.characterCard.cardDetails.model

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.presentation.common.image.ImmutableBitmap

@Immutable
data class ImmutableCard(
    val id: Long = 0,

    val photoBytes: ImmutableBitmap = ImmutableBitmap(),

    val name: String = String(),
    val description: String = String(),
    val personality: String = String(),
    val scenario: String = String(),
    val firstMes: String = String(),
    val mesExample: String = String(),

    // New fields start here
    val creatorNotes: String = String(),
    val systemPrompt: String = String(),
    val postHistoryInstructions: String = String(),
    val alternateGreetings: List<ImmutableAltGreeting> = emptyList(),
    //    val character_book?: CharacterBook

    // May 8th additions
    val tags: List<String> = emptyList(),
    val creator: String = String(),
    val characterVersion: String = String(),
//    val extensions: Record<string, any> // see details for explanation

    val deleted: Boolean = false,
    val selectedChat: Long = 0,
    val selectedGreeting: Int = 0,
) {
    fun toCard(): CharacterCard {
        return CharacterCard(
            id = id,
            photoBytes = photoBytes.bitmap,
            name = name,
            description = description,
            personality = personality,
            scenario = scenario,
            firstMes = firstMes,
            mesExample = mesExample,
            creatorNotes = creatorNotes,
            systemPrompt = systemPrompt,
            postHistoryInstructions = postHistoryInstructions,
            alternateGreetings = alternateGreetings.map { it.toDto() },
            tags = tags,
            creator = creator,
            characterVersion = characterVersion,
            deleted = deleted,
            selectedChat = selectedChat,
            selectedGreeting = selectedGreeting
        )
    }
}

fun CharacterCard.toImmutable(): ImmutableCard {
    return ImmutableCard(
        id = id,
        photoBytes = ImmutableBitmap(photoBytes),
        name = name,
        description = description,
        personality = personality,
        scenario = scenario,
        firstMes = firstMes,
        mesExample = mesExample,
        creatorNotes = creatorNotes,
        systemPrompt = systemPrompt,
        postHistoryInstructions = postHistoryInstructions,
        alternateGreetings = alternateGreetings.map { it.toImmutable() },
        tags = tags,
        creator = creator,
        characterVersion = characterVersion,
        deleted = deleted,
        selectedChat = selectedChat,
        selectedGreeting = selectedGreeting
    )
}


