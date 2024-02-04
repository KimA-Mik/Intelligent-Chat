package ru.kima.intelligentchat.presentation.characterCard.cardDetails.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.card.model.CharacterCard

@Immutable
data class ImmutableCard(
    val id: Long = 0,

    val photoBytes: Bitmap? = null,

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
) {
    fun toCard(): CharacterCard {
        return CharacterCard(
            id = id,
            photoBytes = photoBytes,
            name = name,
            description = description,
            personality = personality,
            scenario = scenario,
            firstMes = firstMes,
            mesExample = mesExample,
            creatorNotes = creatorNotes,
            systemPrompt = systemPrompt,
            postHistoryInstructions = postHistoryInstructions,
            alternateGreetings = alternateGreetings.map { AltGreeting(id, it.body) },
            tags = tags,
            creator = creator,
            characterVersion = characterVersion
        )
    }
}


