package ru.kima.intelligentchat.data.card.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.kima.intelligentchat.data.TAGS_TABLE_NAME
import ru.kima.intelligentchat.data.card.entities.TagEntity

@Dao
interface TagDao {
    @Insert
    suspend fun insertTag(tag: TagEntity)

    @Update
    suspend fun updateTag(tag: TagEntity)

    @Query("SELECT * FROM $TAGS_TABLE_NAME")
    suspend fun selectTags(): List<TagEntity>

    @Query("SELECT * FROM $TAGS_TABLE_NAME WHERE id = :id")
    suspend fun selectTag(id: Int): List<TagEntity>

    @Query("SELECT * FROM $TAGS_TABLE_NAME WHERE id IN (:ids)")
    suspend fun selectTags(ids: List<Int>): List<TagEntity>

    @Query("DELETE FROM $TAGS_TABLE_NAME WHERE id = :id")
    suspend fun deleteTagsById(id: Int)
}