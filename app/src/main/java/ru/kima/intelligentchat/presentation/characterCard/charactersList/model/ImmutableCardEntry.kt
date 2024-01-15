package ru.kima.intelligentchat.presentation.characterCard.charactersList.model

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.presentation.common.image.ImmutableBitmap

@Immutable
data class ImmutableCardEntry(
    val id: Long = 0,
    val thumbnail: ImmutableBitmap = ImmutableBitmap(),
    val name: String = String(),
    val creatorNotes: String = String(),
    val creator: String = String(),
    val characterVersion: String = String()
)
