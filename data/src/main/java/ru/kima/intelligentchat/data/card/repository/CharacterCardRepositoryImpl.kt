package ru.kima.intelligentchat.data.card.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import ru.kima.intelligentchat.data.card.entities.CharacterCardEntity
import ru.kima.intelligentchat.data.card.util.getCardPhotoName
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.image.dataSource.ImageStorage
import ru.kima.intelligentchat.data.serialization.CardDeserializer
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import java.io.ByteArrayOutputStream

class CharacterCardRepositoryImpl(
    wrapper: DatabaseWrapper,
    json: Json,
    private val imageStorage: ImageStorage
) :
    CharacterCardRepository {
    private val jsonDeserializer = CardDeserializer(json)
    private val cardDao = wrapper.database.characterCardDao()

    override fun getCharactersCards() =
        cardDao.selectCharacterCards().map { cards ->
            cards.map { entity ->
                entity.toCharacterCard(imageStorage)
            }
        }

    override suspend fun getCharacterCard(id: Long) =
        cardDao.selectCharacterCard(id).toCharacterCard(imageStorage)

    override suspend fun putCharacterCard(characterCard: CharacterCard): Long {
        val entity = CharacterCardEntity.fromCharacterCard(characterCard)
        return cardDao.insertCharacterCard(entity)
    }

    override suspend fun putCharacterCardFromJson(serialized: String): Long {
        val entity = jsonDeserializer.deserialize(serialized)
        return cardDao.insertCharacterCard(entity)
    }

    override suspend fun updateCharacterCard(characterCard: CharacterCard) {
        val entity = CharacterCardEntity.fromCharacterCard(characterCard)
        cardDao.updateCharacterCard(entity)
    }

    override suspend fun deleteCard(cardId: Long) {
        cardDao.deleteCharacterCardById(cardId)
    }

    override suspend fun updateCardAvatar(card: CharacterCard, bytes: ByteArray): CharacterCard {
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return card

        val fileName = getCardPhotoName(card)

        val outputStream = ByteArrayOutputStream()
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
            return card
        }

        val photoBytes = outputStream.toByteArray()
        val photo = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)
        imageStorage.saveImage(fileName, photoBytes)
        val newCard = card.copy(photoBytes = photo)
        updateCharacterCard(newCard)
        return newCard
    }
}