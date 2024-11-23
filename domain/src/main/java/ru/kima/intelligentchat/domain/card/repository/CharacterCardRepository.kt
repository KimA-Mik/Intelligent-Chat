package ru.kima.intelligentchat.domain.card.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.card.model.CardEntry
import ru.kima.intelligentchat.domain.card.model.CharacterCard

interface CharacterCardRepository {
    fun getCharactersCards(): Flow<List<CharacterCard>>
    fun getCardsListEntries(): Flow<List<CardEntry>>
    fun getCharacterCard(id: Long): Flow<CharacterCard>
    suspend fun putCharacterCard(characterCard: CharacterCard): Long
    suspend fun putCharacterCardFromJson(serialized: String): Long
    suspend fun updateCharacterCard(characterCard: CharacterCard)
    suspend fun deleteCards(cards: List<CharacterCard>)
    suspend fun updateCardAvatar(cardId: Long, photoName: String?)
    suspend fun getMarkedCards(): List<CharacterCard>
    suspend fun createAlternateGreeting(cardId: Long): Long
    suspend fun updateAlternateGreeting(altGreeting: AltGreeting, cardId: Long)
    suspend fun deleteAlternateGreeting(id: Long)
}