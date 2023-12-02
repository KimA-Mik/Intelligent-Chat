package ru.kima.intelligentchat.domain.card.model

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterCard

        if (id != other.id) return false
        if (photoBytes != null) {
            if (other.photoBytes == null) return false
            if (!photoBytes.contentEquals(other.photoBytes)) return false
        } else if (other.photoBytes != null) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (personality != other.personality) return false
        if (scenario != other.scenario) return false
        if (firstMes != other.firstMes) return false
        if (mesExample != other.mesExample) return false
        if (creatorNotes != other.creatorNotes) return false
        if (systemPrompt != other.systemPrompt) return false
        if (postHistoryInstructions != other.postHistoryInstructions) return false
        if (alternateGreetings != other.alternateGreetings) return false
        if (tags != other.tags) return false
        if (creator != other.creator) return false
        return characterVersion == other.characterVersion
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (photoBytes?.contentHashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + personality.hashCode()
        result = 31 * result + scenario.hashCode()
        result = 31 * result + firstMes.hashCode()
        result = 31 * result + mesExample.hashCode()
        result = 31 * result + creatorNotes.hashCode()
        result = 31 * result + systemPrompt.hashCode()
        result = 31 * result + postHistoryInstructions.hashCode()
        result = 31 * result + alternateGreetings.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + creator.hashCode()
        result = 31 * result + characterVersion.hashCode()
        return result
    }
}

