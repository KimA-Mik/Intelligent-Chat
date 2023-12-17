package ru.kima.intelligentchat.data.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.kima.intelligentchat.data.card.entities.CharacterEntity

//TODO: Explore custom Kotlinx serializers
class CardDeserializer(private val serializer: Json) {
    private val v2Spec = "chara_card_v2"

    fun deserialize(json: String): CharacterEntity {
        val root = serializer.parseToJsonElement(json)
        val spec = root.jsonObject["spec"]

        return if (spec == null) {
            parseV1(root)
        } else {
            val specContent = spec.jsonPrimitive.content

            when (specContent) {
                v2Spec -> {
                    parseV2(root)
                }

                else -> throw Exception("Unsupported spec version")
            }
        }
    }

    //todo: change result to card
    private fun parseV1(root: JsonElement): CharacterEntity {
        val deserialized: CardV1 = serializer.decodeFromJsonElement(root)
        return deserialized.toCharacterCardEntity()
    }

    private fun parseV2(root: JsonElement): CharacterEntity {
        val deserialized: CardV2 = serializer.decodeFromJsonElement(root)
        return deserialized.toCharacterCardEntity()
    }
}