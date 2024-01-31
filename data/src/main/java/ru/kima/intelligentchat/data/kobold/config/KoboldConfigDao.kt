package ru.kima.intelligentchat.data.kobold.config

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.KOBOLD_CONFIG_TABLE_NAME

@Dao
interface KoboldConfigDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(config: KoboldConfigEntity): Long

    @Update
    suspend fun update(config: KoboldConfigEntity)

    @Query("SELECT * FROM $KOBOLD_CONFIG_TABLE_NAME WHERE id=:id")
    suspend fun select(id: Long): KoboldConfigEntity?

    @Query("SELECT * FROM $KOBOLD_CONFIG_TABLE_NAME WHERE id=:id")
    fun subscribe(id: Long): Flow<KoboldConfigEntity>

    @Query("DELETE FROM $KOBOLD_CONFIG_TABLE_NAME WHERE id=:id")
    suspend fun delete(id: Long)
}