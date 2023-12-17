package ru.kima.intelligentchat.data.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.data.card.entities.CharacterEntity

@Serializable
data class CardV1(
    val name: String = String(),
    val description: String = String(),
    val personality: String = String(),
    val scenario: String = String(),
    @SerialName("first_mes")
    val firstMes: String = String(),
    @SerialName("mes_example")
    val mesExample: String = String(),
) {
    fun toCharacterCardEntity(): CharacterEntity {
        return CharacterEntity(
            name = name,
            description = description,
            personality = personality,
            scenario = scenario,
            firstMes = firstMes,
            mesExample = mesExample
        )
    }
}
