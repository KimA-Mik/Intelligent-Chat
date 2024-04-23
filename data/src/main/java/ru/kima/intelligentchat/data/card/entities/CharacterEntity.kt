package ru.kima.intelligentchat.data.card.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
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

    val deleted: Boolean = false
) {
    //https://issuetracker.google.com/issues/70762008
    @Ignore
    var alternateGreetings: List<String> = emptyList()
}

