package ru.kima.intelligentchat.domain.card.model

import android.graphics.Bitmap

data class CharacterCard(
    val id: Long = 0,

    val photoBytes: Bitmap? = null,

    val name: String = String(),
    val description: String = String(),
    val personality: String = String(),
    val scenario: String = String(),
    val firstMes: String = String(),
    val mesExample: String = String(),

    // New fields start here
    val creatorNotes: String = String(),
    val systemPrompt: String = String(),
    val postHistoryInstructions: String = String(),
    val alternateGreetings: List<AltGreeting> = emptyList(),
    //    val character_book?: CharacterBook

    // May 8th additions
    val tags: List<String> = emptyList(),
    val creator: String = String(),
    val characterVersion: String = String(),
//    val extensions: Record<string, any> // see details for explanation

    val deleted: Boolean = false,
    val selectedChat: Long = 0
)