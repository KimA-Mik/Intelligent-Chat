package ru.kima.intelligentchat.domain.card.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.card.model.CharacterCard

interface CharacterCardRepository {
    fun getCharactersCards(): Flow<List<CharacterCard>>
    fun getCharacterCard(id: Long): Flow<CharacterCard>
    suspend fun putCharacterCard(characterCard: CharacterCard): Long
    suspend fun putCharacterCardFromJson(serialized: String): Long
    suspend fun updateCharacterCard(characterCard: CharacterCard)
    suspend fun deleteCard(card: CharacterCard)
    suspend fun updateCardAvatar(cardId: Long, bytes: ByteArray)
}