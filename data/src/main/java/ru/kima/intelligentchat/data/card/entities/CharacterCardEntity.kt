package ru.kima.intelligentchat.data.card.entities

import android.graphics.BitmapFactory
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.kima.intelligentchat.data.CHARACTERS_TABLE_NAME
import ru.kima.intelligentchat.data.card.util.getCardPhotoName
import ru.kima.intelligentchat.data.image.dataSource.ImageStorage
import ru.kima.intelligentchat.domain.card.model.CharacterCard

@Entity(CHARACTERS_TABLE_NAME)
data class CharacterCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val photoFilePath: String? = null,
    val name: String = String(),
    val description: String = String(),
    val personality: String = String(),
    val scenario: String = String(),
    val firstMes: String = String(),
    val mesExample: String = String(),
    val creatorNotes: String = String(),
    val systemPrompt: String = String(),
    val postHistoryInstructions: String = String(),
//    val alternateGreetings: List<String>,
    val creator: String = String(),
    val characterVersion: String = String(),
//    val extensions: Record<string, any> // see details for explanation
) {
    suspend fun toCharacterCard(
        imageStorage: ImageStorage,
        tags: List<String> = emptyList()
    ): CharacterCard {
        return CharacterCard(
            id,
            photoFilePath?.let {
                coroutineScope {
                    val job = async(Dispatchers.Unconfined, start = CoroutineStart.LAZY) {
                        val image = imageStorage.getImage(it)
                        BitmapFactory.decodeByteArray(image, 0, image.size)
                    }
                    job.await()
                }
            },
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
                photoFilePath = card.photoBytes?.let { getCardPhotoName(card.id) },
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
