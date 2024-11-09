package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import ru.kima.intelligentchat.domain.messaging.model.MessagingIndicator
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ImmutableMessagingIndicator

fun MessagingIndicator.toImmutable(): ImmutableMessagingIndicator {
    return when (this) {
        is MessagingIndicator.DeterminedGenerating -> ImmutableMessagingIndicator.DeterminedGenerating(
            progress
        )

        MessagingIndicator.Generating -> ImmutableMessagingIndicator.Generating
        MessagingIndicator.None -> ImmutableMessagingIndicator.None
        MessagingIndicator.Pending -> ImmutableMessagingIndicator.Pending
    }
}