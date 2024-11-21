package ru.kima.intelligentchat.data.card.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import ru.kima.intelligentchat.data.card.entities.AltGreetingEntity
import ru.kima.intelligentchat.data.card.entities.CardEntity
import ru.kima.intelligentchat.data.card.entities.CharacterEntity
import ru.kima.intelligentchat.data.card.mappers.toCharacterCard
import ru.kima.intelligentchat.data.card.mappers.toEntity
import ru.kima.intelligentchat.data.card.mappers.toEntry
import ru.kima.intelligentchat.data.card.util.getCardPhotoName
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.image.dataSource.DummyImageStorage
import ru.kima.intelligentchat.data.image.dataSource.InternalImageStorage
import ru.kima.intelligentchat.data.serialization.CardDeserializer
import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.card.model.CardEntry
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import java.io.ByteArrayOutputStream

class CharacterCardRepositoryImpl(
    wrapper: DatabaseWrapper,
    json: Json,
    private val imageStorage: InternalImageStorage
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

    override fun getCardsListEntries(): Flow<List<CardEntry>> =
        cardDao.getCharacterListEntries().map { entries ->
            entries.map { entry ->
                entry.toEntry(imageStorage)
            }
        }

    override fun getCharacterCard(id: Long) =
        cardDao.selectCharacterCard(id).map {
            it.toCharacterCard(imageStorage)
        }

    override suspend fun putCharacterCard(characterCard: CharacterCard): Long {
        val entity = characterCard.toEntity()
        return cardDao.insertTransaction(entity)
    }

    override suspend fun putCharacterCardFromJson(serialized: String): Long {
        val entity = jsonDeserializer.deserialize(serialized)
        val card = CardEntity(
            character = entity,
            altGreetings = entity.alternateGreetings.map {
                AltGreetingEntity(
                    id = 0,
                    cardId = 0,
                    body = it
                )
            }
        )
        return cardDao.insertTransaction(card)
    }

    override suspend fun updateCharacterCard(characterCard: CharacterCard) {
        val entity = characterCard.toEntity()
        cardDao.updateTransaction(entity)
    }

    override suspend fun deleteCards(cards: List<CharacterCard>) {
        for (card in cards) {
            card.photoBytes?.let {
                val name = getCardPhotoName(card.id)
                imageStorage.deleteImage(name)
            }
        }

        val deleted = cards.map {
            CharacterEntity(
                id = it.id,
                name = it.name,
                deleted = true
            )
        }

        cardDao.softDeleteTransaction(deleted)
    }

    override suspend fun updateCardAvatar(cardId: Long, bytes: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return

        val fileName = getCardPhotoName(cardId)

        val outputStream = ByteArrayOutputStream()
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
            return
        }

        val photoBytes = outputStream.toByteArray()
        imageStorage.saveImage(fileName, photoBytes)
        cardDao.updatePhotoFilePath(cardId, fileName)
    }

    override suspend fun getMarkedCards(): List<CharacterCard> {
        val dummyImageStorage = DummyImageStorage()
        return cardDao.markedCards().map {
            it.toCharacterCard(dummyImageStorage, emptyList())
        }
    }

    override suspend fun createAlternateGreeting(cardId: Long): Long {
        val greeting = AltGreetingEntity(cardId = cardId, body = String())
        return cardDao.insertGreeting(greeting)
    }

    override suspend fun updateAlternateGreeting(altGreeting: AltGreeting, cardId: Long) {
        val entity = altGreeting.toEntity(cardId)
        cardDao.updateGreeting(entity)
    }

    override suspend fun deleteAlternateGreeting(id: Long) {
        cardDao.deleteGreeting(id)
    }
}