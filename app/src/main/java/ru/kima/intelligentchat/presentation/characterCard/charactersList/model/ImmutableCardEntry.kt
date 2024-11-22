package ru.kima.intelligentchat.presentation.characterCard.charactersList.model

import androidx.compose.runtime.Immutable

@Immutable
data class ImmutableCardEntry(
    val id: Long = 0,
    val photoName: String? = null,
    val name: String = String(),
    val creatorNotes: String = String(),
    val creator: String = String(),
    val characterVersion: String = String()
)
