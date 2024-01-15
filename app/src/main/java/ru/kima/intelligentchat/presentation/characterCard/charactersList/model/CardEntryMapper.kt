package ru.kima.intelligentchat.presentation.characterCard.charactersList.model

import ru.kima.intelligentchat.domain.card.model.CardEntry
import ru.kima.intelligentchat.presentation.common.image.ImmutableBitmap

fun CardEntry.toImmutable(): ImmutableCardEntry {
    return ImmutableCardEntry(
        id = id,
        thumbnail = ImmutableBitmap(thumbnail),
        name = name,
        creatorNotes = creatorNotes,
        creator = creator,
        characterVersion = characterVersion
    )
}