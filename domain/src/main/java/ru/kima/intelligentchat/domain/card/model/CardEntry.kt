package ru.kima.intelligentchat.domain.card.model

data class CardEntry(
    val id: Long = 0,
    val photoName: String? = null,
    val name: String = String(),
    val creatorNotes: String = String(),
    val creator: String = String(),
    val characterVersion: String = String()
)
