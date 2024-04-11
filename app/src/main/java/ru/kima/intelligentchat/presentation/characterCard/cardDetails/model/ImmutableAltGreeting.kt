package ru.kima.intelligentchat.presentation.characterCard.cardDetails.model

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.domain.card.model.AltGreeting

@Immutable
data class ImmutableAltGreeting(
    val id: Long,
    val body: String
) {
    fun toDto(): AltGreeting {
        return AltGreeting(
            id, body
        )
    }
}

fun AltGreeting.toImmutable(): ImmutableAltGreeting {
    return ImmutableAltGreeting(id, body)
}