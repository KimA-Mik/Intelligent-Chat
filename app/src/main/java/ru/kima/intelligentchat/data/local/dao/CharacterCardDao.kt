package ru.kima.intelligentchat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.kima.intelligentchat.data.local.CHARACTERS_TABLE_NAME
import ru.kima.intelligentchat.data.local.entities.CharacterCardEntity

@Dao
interface CharacterCardDao {
    @Insert
    suspend fun insertCharacterCard(characterCard: CharacterCardEntity)

    @Update
    suspend fun updateCharacterCard(characterCard: CharacterCardEntity)

    @Query("SELECT * FROM $CHARACTERS_TABLE_NAME")
    suspend fun selectCharacterCards(): List<CharacterCardEntity>

    @Query("SELECT * FROM $CHARACTERS_TABLE_NAME WHERE id = :id")
    suspend fun selectCharacterCard(id: Int): CharacterCardEntity

    @Query("DELETE FROM $CHARACTERS_TABLE_NAME WHERE id = :id")
    suspend fun deleteCharacterCardById(id: Int)
}