package ru.kima.intelligentchat.data.chat.instructMode

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.INSTRUCT_MODE_TEMPLATES_TABLE_NAME

@Dao
interface InstructModeTemplateDao {
    @Insert
    suspend fun insert(templateEntity: InstructModeTemplateEntity): Long

    @Update
    suspend fun update(templateEntity: InstructModeTemplateEntity)

    @Delete
    suspend fun delete(templateEntity: InstructModeTemplateEntity)

    @Query("SELECT * FROM $INSTRUCT_MODE_TEMPLATES_TABLE_NAME")
    fun subscribeToAll(): Flow<List<InstructModeTemplateEntity>>

    @Query("SELECT * FROM $INSTRUCT_MODE_TEMPLATES_TABLE_NAME WHERE id=:id")
    suspend fun get(id: Long): InstructModeTemplateEntity?
}