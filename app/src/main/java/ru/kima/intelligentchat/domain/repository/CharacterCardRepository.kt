package ru.kima.intelligentchat.domain.repository

import ru.kima.intelligentchat.domain.model.CharacterCard

interface CharacterCardRepository {
    suspend fun getCharactersCards(): List<CharacterCard>
    suspend fun getCharacterCard(id: Long): CharacterCard
    suspend fun putCharacterCard(characterCard: CharacterCard): Long
    suspend fun putCharacterCardFromJson(serialized: String): Long
    suspend fun updateCharacterCard(characterCard: CharacterCard)
}