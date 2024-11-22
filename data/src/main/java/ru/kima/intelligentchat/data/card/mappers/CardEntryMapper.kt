package ru.kima.intelligentchat.data.card.mappers

import ru.kima.intelligentchat.data.card.entities.CardListItemEntity
import ru.kima.intelligentchat.domain.card.model.CardEntry

fun CardListItemEntity.toEntry(): CardEntry {
    return CardEntry(
        id = id,
        photoName = photoFilePath,
        name = name,
        creatorNotes = creatorNotes,
        creator = creator,
        characterVersion = characterVersion
    )
}
