package ru.kima.intelligentchat.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.local.CHARACTERS_TABLE_NAME
import ru.kima.intelligentchat.domain.model.CharacterCard

@Entity(CHARACTERS_TABLE_NAME)
data class CharacterCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val photoFilePath: String?,
    val name: String,
    val description: String,
    val personality: String,
    val scenario: String,
    val firstMes: String,
    val mesExample: String,
    val creatorNotes: String,
    val systemPrompt: String,
    val postHistoryInstructions: String,
//    val alternateGreetings: List<String>,
    val creator: String,
    val characterVersion: String,
//    val extensions: Record<string, any> // see details for explanation
) {
    fun toCharacterCard(tags: List<String> = emptyList()): CharacterCard {
        return CharacterCard(
            id,
            photoFilePath,
            name,
            description,
            personality,
            scenario,
            firstMes,
            mesExample,
            creatorNotes,
            systemPrompt,
            postHistoryInstructions,
//            alternateGreetings,
            emptyList(),
            tags,
            creator,
            characterVersion
        )
    }

    companion object {
        fun fromCharacterCard(card: CharacterCard): CharacterCardEntity {
            return CharacterCardEntity(
                id = card.id,
                photoFilePath = card.photoFilePath,
                name = card.name,
                description = card.description,
                personality = card.personality,
                scenario = card.scenario,
                firstMes = card.firstMes,
                mesExample = card.mesExample,
                creatorNotes = card.creatorNotes,
                systemPrompt = card.systemPrompt,
                postHistoryInstructions = card.postHistoryInstructions,
//                alternateGreetings = card.alternateGreetings,
                creator = card.creator,
                characterVersion = card.characterVersion
            )
        }
    }
}
