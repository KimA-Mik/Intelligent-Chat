package ru.kima.intelligentchat.data.kobold.preset.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.TEXT_COMPLETION_PRESET_TABLE_NAME
import ru.kima.intelligentchat.data.kobold.preset.entities.TextCompletionPresetEntity

@Dao
interface TextCompletionPresetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(preset: TextCompletionPresetEntity): Long

    @Update
    suspend fun update(preset: TextCompletionPresetEntity)

    @Query("SELECT * FROM $TEXT_COMPLETION_PRESET_TABLE_NAME")
    fun subscribeToAll(): Flow<List<TextCompletionPresetEntity>>

    @Query("SELECT * FROM $TEXT_COMPLETION_PRESET_TABLE_NAME WHERE id=:id")
    suspend fun select(id: Long): TextCompletionPresetEntity?
}
