package ru.kima.intelligentchat.data.card.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CardEntity(
    @Embedded val character: CharacterEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "cardId"
    )
    val altGreetings: List<AltGreetingEntity>
)
