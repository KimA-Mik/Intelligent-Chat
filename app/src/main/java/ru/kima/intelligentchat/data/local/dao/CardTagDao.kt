package ru.kima.intelligentchat.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.kima.intelligentchat.data.local.CARD_TAGS_TABLE_NAME
import ru.kima.intelligentchat.data.local.entities.CardTagEntity
import ru.kima.intelligentchat.data.local.entities.TagEntity

@Dao
interface CardTagDao {
    @Insert
    suspend fun insertTag(cardTagEntity: CardTagEntity)

    @Update
    suspend fun updateTag(tag: TagEntity)

    @Query("SELECT * FROM $CARD_TAGS_TABLE_NAME")
    suspend fun selectCharactersTags(): List<CardTagEntity>

    @Query("SELECT * FROM $CARD_TAGS_TABLE_NAME WHERE id = :id")
    suspend fun selectCharacterTag(id: Int): List<CardTagEntity>

    //Maybe select tag/character directly
    @Query("SELECT * FROM $CARD_TAGS_TABLE_NAME WHERE characterId = :characterId")
    suspend fun selectTagsForCharacter(characterId: Int): List<CardTagEntity>

    @Query("SELECT * FROM $CARD_TAGS_TABLE_NAME WHERE tagId = :tagId")
    suspend fun selectCharactersWithTag(tagId: Int): List<CardTagEntity>
}