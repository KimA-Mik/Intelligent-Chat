package ru.kima.intelligentchat.data.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.data.local.entities.CharacterCardEntity

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
    fun toCharacterCardEntity(): CharacterCardEntity {
        return CharacterCardEntity(
            name = name,
            description = description,
            personality = personality,
            scenario = scenario,
            firstMes = firstMes,
            mesExample = mesExample
        )
    }
}
