package ru.kima.intelligentchat.data.persona

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.PERSONAS_TABLE_NAME

@Dao
interface PersonaDao {
    @Insert
    suspend fun insertPersona(persona: PersonaEntity): Long

    @Update
    suspend fun updatePersona(persona: PersonaEntity)

    @Query("SELECT * FROM $PERSONAS_TABLE_NAME WHERE id = :id")
    fun subscribeToPersona(id: Long): Flow<PersonaEntity?>

    @Query("SELECT * FROM $PERSONAS_TABLE_NAME WHERE id = :id")
    suspend fun selectPersona(id: Long): PersonaEntity

    @Query("SELECT * FROM $PERSONAS_TABLE_NAME")
    fun selectPersonas(): Flow<List<PersonaEntity>>

    @Query("DELETE FROM $PERSONAS_TABLE_NAME WHERE id = :id")
    suspend fun deletePersona(id: Long)

    @Query("UPDATE $PERSONAS_TABLE_NAME SET imageFilePath=:imageFilePath WHERE id = :id")
    suspend fun updateImageFilePath(id: Long, imageFilePath: String)

    @Query("SELECT COUNT(id) FROM $PERSONAS_TABLE_NAME")
    suspend fun getPersonasCount(): Int
}