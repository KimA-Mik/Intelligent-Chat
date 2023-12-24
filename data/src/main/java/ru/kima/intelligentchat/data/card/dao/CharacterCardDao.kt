package ru.kima.intelligentchat.data.card.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.ALT_GREETING_TABLE_NAME
import ru.kima.intelligentchat.data.card.entities.AltGreetingEntity
import ru.kima.intelligentchat.data.card.entities.CardEntity
import ru.kima.intelligentchat.data.card.entities.CardListItemEntity
import ru.kima.intelligentchat.data.card.entities.CharacterEntity

@Dao
interface CharacterCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAltGreetings(greetings: List<AltGreetingEntity>)

    @Insert
    suspend fun insertCharacterCard(character: CharacterEntity): Long

    @Update
    suspend fun updateCharacterCard(character: CharacterEntity)

    @Transaction
    suspend fun insertTransaction(card: CardEntity): Long {
        val id = insertCharacterCard(card.character)
        if (card.altGreetings.isNotEmpty()) {
            val greetings = card.altGreetings.map { it.copy(cardId = id) }
            insertAltGreetings(greetings)
        }
        return id
    }

    @Transaction
    suspend fun updateTransaction(card: CardEntity) {
        updateCharacterCard(card.character)
        if (card.altGreetings.isNotEmpty()) {
            insertAltGreetings(card.altGreetings)
        }
    }

    @Query("UPDATE CharacterEntity SET photoFilePath=:photoFilePath WHERE id = :id")
    suspend fun updatePhotoFilePath(id: Long, photoFilePath: String)

    @Transaction
    @Query("SELECT * FROM CharacterEntity")
    fun selectCharacterCards(): Flow<List<CardEntity>>

    @Transaction
    @Query("SELECT * FROM CharacterEntity WHERE id = :id")
    fun selectCharacterCard(id: Long): Flow<CardEntity>

    @Query("SELECT id, photoFilePath, name, creatorNotes, creator, characterVersion FROM CharacterEntity")
    fun getCharacterListEntries(): Flow<List<CardListItemEntity>>

    @Transaction
    suspend fun deleteTransaction(id: Long) {
        deleteCharacterCardById(id)
        deleteGreetings(id)
    }

    @Query("DELETE FROM CharacterEntity WHERE id = :id")
    suspend fun deleteCharacterCardById(id: Long)

    @Query("DELETE FROM $ALT_GREETING_TABLE_NAME where cardId=:cardId")
    suspend fun deleteGreetings(cardId: Long)
}