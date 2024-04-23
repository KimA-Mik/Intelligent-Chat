package ru.kima.intelligentchat.data.persona

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.PERSONAS_TABLE_NAME

@Entity(PERSONAS_TABLE_NAME)
data class PersonaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = String(),
    val description: String = String(),
    val imageFilePath: String? = null,
    val personaWordsCount: Long = 0,
    val charactersWordsCount: Long = 0,
    val personaMessages: Long = 0,
    val charactersMessages: Long = 0,
    val swipes: Long = 0,
    val deleted: Boolean = false
)