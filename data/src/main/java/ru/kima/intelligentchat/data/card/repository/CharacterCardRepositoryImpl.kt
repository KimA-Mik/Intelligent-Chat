package ru.kima.intelligentchat.data.card.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import ru.kima.intelligentchat.data.card.entities.AltGreetingEntity
import ru.kima.intelligentchat.data.card.entities.CardEntity
import ru.kima.intelligentchat.data.card.entities.CharacterEntity
import ru.kima.intelligentchat.data.card.mappers.toCharacterCard
import ru.kima.intelligentchat.data.card.mappers.toEntity
import ru.kima.intelligentchat.data.card.mappers.toEntry
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.image.dataSource.InternalImageStorage
import ru.kima.intelligentchat.data.serialization.CardDeserializer
import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.card.model.CardEntry
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

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
                entity.toCharacterCard()
            }
        }

    override fun getCardsListEntries(): Flow<List<CardEntry>> =
        cardDao.getCharacterListEntries().map { entries ->
            entries.map { entry ->
                entry.toEntry()
            }
        }

    override fun getCharacterCard(id: Long) =
        cardDao.selectCharacterCard(id).map {
            it.toCharacterCard()
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
            card.photoName?.let {
                imageStorage.deleteImage(it)
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

    override suspend fun updateCardAvatar(cardId: Long, photoName: String?) {
        cardDao.updatePhotoFilePath(cardId, photoName)
    }

    override suspend fun getMarkedCards(): List<CharacterCard> {
        return cardDao.markedCards().map {
            it.toCharacterCard(emptyList())
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