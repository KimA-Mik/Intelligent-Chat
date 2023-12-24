package ru.kima.intelligentchat.data.card.mappers

import ru.kima.intelligentchat.data.card.entities.CardListItemEntity
import ru.kima.intelligentchat.data.image.dataSource.ImageStorage
import ru.kima.intelligentchat.domain.card.model.CardEntry

suspend fun CardListItemEntity.toEntry(imageStorage: ImageStorage): CardEntry {
    return CardEntry(
        id = id,
        thumbnail = photoFilePath?.let {
            imageStorage.getThumbnail(it)
        },
        name = name,
        creatorNotes = creatorNotes,
        creator = creator,
        characterVersion = characterVersion
    )
}
