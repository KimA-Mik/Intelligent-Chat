package ru.kima.intelligentchat.data.card.entities

data class CardListItemEntity(
    val id: Long = 0,
    val photoFilePath: String? = null,
    val name: String = String(),
    val creatorNotes: String = String(),
    val creator: String = String(),
    val characterVersion: String = String()
)
