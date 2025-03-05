package ru.kima.intelligentchat.data.chat.advancedFormatting.contextTemplate

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.CONTEXT_TEMPLATES_TABLE_NAME

@Dao
interface ContextTemplateDao {
    @Insert
    suspend fun insert(contextTemplateEntity: ContextTemplateEntity): Long

    @Update
    suspend fun update(contextTemplateEntity: ContextTemplateEntity): Int

    @Delete
    suspend fun delete(contextTemplateEntity: ContextTemplateEntity): Int

    @Query("SELECT * FROM $CONTEXT_TEMPLATES_TABLE_NAME WHERE id=:id")
    suspend fun selectById(id: Long): ContextTemplateEntity?

    @Query("SELECT * FROM $CONTEXT_TEMPLATES_TABLE_NAME")
    fun subscribeToAll(): Flow<List<ContextTemplateEntity>>
}