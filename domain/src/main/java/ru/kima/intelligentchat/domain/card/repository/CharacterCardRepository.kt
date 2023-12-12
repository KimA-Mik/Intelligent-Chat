package ru.kima.intelligentchat.domain.card.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.card.model.CharacterCard

interface CharacterCardRepository {
    fun getCharactersCards(): Flow<List<CharacterCard>>
    suspend fun getCharacterCard(id: Long): CharacterCard
    suspend fun putCharacterCard(characterCard: CharacterCard): Long
    suspend fun putCharacterCardFromJson(serialized: String): Long
    suspend fun updateCharacterCard(characterCard: CharacterCard)
    suspend fun deleteCard(cardId: Long)
    suspend fun updateCardAvatar(card: CharacterCard, bytes: ByteArray): CharacterCard
}