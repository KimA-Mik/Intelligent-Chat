package ru.kima.intelligentchat.data.card.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.CARDS_TABLE_NAME

@Entity(tableName = CARDS_TABLE_NAME)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val photoFilePath: String? = null,
    val name: String = String(),
    val description: String = String(),
    val personality: String = String(),
    val scenario: String = String(),
    val firstMes: String = String(),
    val mesExample: String = String(),
    val creatorNotes: String = String(),
    val systemPrompt: String = String(),
    val postHistoryInstructions: String = String(),
    val creator: String = String(),
    val characterVersion: String = String(),
//    val extensions: Record<string, any> // see details for explanation

    val deleted: Boolean = false,
    val selectedChat: Long = 0,
    val selectedGreeting: Int = 1,
) {
    //https://issuetracker.google.com/issues/70762008
    @Ignore
    var alternateGreetings: List<String> = emptyList()
}

