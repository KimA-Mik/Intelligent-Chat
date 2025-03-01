package ru.kima.intelligentchat.presentation.characterCard.cardDetails.model

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.domain.card.model.CharacterCard

@Immutable
data class ImmutableCard(
    val id: Long = 0,

    val photoName: String? = null,

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
            photoName = photoName,
            name = name.trimIndent(),
            description = description.trimIndent(),
            personality = personality.trimIndent(),
            scenario = scenario.trimIndent(),
            firstMes = firstMes.trimIndent(),
            mesExample = mesExample.trimIndent(),
            creatorNotes = creatorNotes,
            systemPrompt = systemPrompt.trimIndent(),
            postHistoryInstructions = postHistoryInstructions.trimIndent(),
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
        photoName = photoName,
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


