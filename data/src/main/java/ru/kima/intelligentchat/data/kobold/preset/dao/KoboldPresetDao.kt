package ru.kima.intelligentchat.data.kobold.preset.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.KOBOLD_PRESET_TABLE_NAME
import ru.kima.intelligentchat.data.kobold.preset.entities.KoboldPresetEntity

@Dao
interface KoboldPresetDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(config: KoboldPresetEntity): Long

    @Update
    suspend fun update(config: KoboldPresetEntity)

    @Query("SELECT * FROM $KOBOLD_PRESET_TABLE_NAME")
    fun subscribeToAll(): Flow<List<KoboldPresetEntity>>

    @Query("SELECT * FROM $KOBOLD_PRESET_TABLE_NAME WHERE id=:id")
    suspend fun select(id: Long): KoboldPresetEntity?

    @Query("SELECT * FROM $KOBOLD_PRESET_TABLE_NAME WHERE id=:id")
    fun subscribe(id: Long): Flow<KoboldPresetEntity>

    @Query("DELETE FROM $KOBOLD_PRESET_TABLE_NAME WHERE id=:id")
    suspend fun delete(id: Long)
}