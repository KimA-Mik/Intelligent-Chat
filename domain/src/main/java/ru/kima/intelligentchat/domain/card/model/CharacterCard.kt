package ru.kima.intelligentchat.domain.card.model

import android.graphics.Bitmap

data class CharacterCard(
    val id: Long,

    val photoBytes: Bitmap?,

    val name: String,
    val description: String,
    val personality: String,
    val scenario: String,
    val firstMes: String,
    val mesExample: String,

    // New fields start here
    val creatorNotes: String,
    val systemPrompt: String,
    val postHistoryInstructions: String,
    val alternateGreetings: List<AltGreeting>,
    //    val character_book?: CharacterBook

    // May 8th additions
    val tags: List<String>,
    val creator: String,
    val characterVersion: String,
//    val extensions: Record<string, any> // see details for explanation

    val deleted: Boolean,
    val selectedChat: Long,
    val selectedGreeting: Int,
) {
    companion object {
        fun default(
            id: Long = 0,
            photoBytes: Bitmap? = null,
            name: String = String(),
            description: String = String(),
            personality: String = String(),
            scenario: String = String(),
            firstMes: String = String(),
            mesExample: String = String(),
            creatorNotes: String = String(),
            systemPrompt: String = String(),
            postHistoryInstructions: String = String(),
            alternateGreetings: List<AltGreeting> = emptyList(),
            tags: List<String> = emptyList(),
            creator: String = String(),
            characterVersion: String = String(),
            deleted: Boolean = false,
            selectedChat: Long = 0,
            selectedGreeting: Int = 0
        ): CharacterCard {
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
                alternateGreetings = alternateGreetings,
                tags = tags,
                creator = creator,
                characterVersion = characterVersion,
                deleted = deleted,
                selectedChat = selectedChat,
                selectedGreeting = selectedGreeting,
            )
        }
    }
}