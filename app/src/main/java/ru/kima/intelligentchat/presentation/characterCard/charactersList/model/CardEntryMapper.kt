package ru.kima.intelligentchat.presentation.characterCard.charactersList.model

import ru.kima.intelligentchat.domain.card.model.CardEntry

fun CardEntry.toImmutable(): ImmutableCardEntry {
    return ImmutableCardEntry(
        id = id,
        photoName = photoName,
        name = name,
        creatorNotes = creatorNotes,
        creator = creator,
        characterVersion = characterVersion
    )
}