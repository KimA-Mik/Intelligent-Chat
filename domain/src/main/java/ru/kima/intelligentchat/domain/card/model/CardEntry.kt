package ru.kima.intelligentchat.domain.card.model

import android.graphics.Bitmap

data class CardEntry(
    val id: Long = 0,
    val photoBytes: Bitmap? = null,
    val name: String = String(),
    val creatorNotes: String = String(),
    val creator: String = String(),
    val characterVersion: String = String()
)
