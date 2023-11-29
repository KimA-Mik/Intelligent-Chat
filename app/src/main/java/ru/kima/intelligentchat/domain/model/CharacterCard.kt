package ru.kima.intelligentchat.domain.model

data class CharacterCard(
    val id: Long = 0,

    val photoBytes: ByteArray? = null,

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
    val alternateGreetings: List<String> = emptyList(),
    //    val character_book?: CharacterBook

    // May 8th additions
    val tags: List<String> = emptyList(),
    val creator: String = String(),
    val characterVersion: String = String(),
//    val extensions: Record<string, any> // see details for explanation
) {
    companion object
}

