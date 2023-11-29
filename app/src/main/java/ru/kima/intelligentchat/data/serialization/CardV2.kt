package ru.kima.intelligentchat.data.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.data.local.entities.CharacterCardEntity

@Serializable
data class CardV2(
    val spec: String = String(),
    @SerialName("spec_version")
    val specVersion: String = String(), // May 8th addition
    val data: Data = Data()
) {
    @Serializable
    data class Data(
        val name: String = String(),
        val description: String = String(),
        val personality: String = String(),
        val scenario: String = String(),
        @SerialName("first_mes")
        val firstMes: String = String(),
        @SerialName("mes_example")
        val mesExample: String = String(),

        // New fields start here
        @SerialName("creator_notes")
        val creatorNotes: String = String(),
        @SerialName("system_prompt")
        val systemPrompt: String = String(),
        @SerialName("post_history_instructions")
        val postHistoryInstructions: String = String(),
        @SerialName("alternate_greetings")
        val alternateGreetings: List<String> = emptyList(),
//        character_book?: CharacterBook

        // May 8th additions
        val tags: List<String> = emptyList(),
        val creator: String = String(),
        @SerialName("character_version")
        val characterVersion: String = String(),
//        extensions: Record<string, any> // see details for explanation
    )

    fun toCharacterCardEntity(): CharacterCardEntity {
        return CharacterCardEntity(
            name = data.name,
            description = data.description,
            personality = data.personality,
            scenario = data.scenario,
            firstMes = data.firstMes,
            mesExample = data.mesExample,
            creatorNotes = data.creatorNotes,
            systemPrompt = data.systemPrompt,
            postHistoryInstructions = data.postHistoryInstructions,
            creator = data.creator,
            characterVersion = data.characterVersion
        )
    }
}
