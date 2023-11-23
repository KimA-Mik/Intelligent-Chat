package ru.kima.intelligentchat.domain.repository

import ru.kima.intelligentchat.domain.model.CharacterCard

interface CharacterCardRepository {
    suspend fun getCharactersCards(): List<CharacterCard>
    suspend fun putCharacterCard(characterCard: CharacterCard)
}