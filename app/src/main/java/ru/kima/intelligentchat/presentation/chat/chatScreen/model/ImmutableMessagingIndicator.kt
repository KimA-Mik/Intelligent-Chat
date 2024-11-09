package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ImmutableMessagingIndicator {
    @Immutable
    data object None : ImmutableMessagingIndicator
    @Immutable
    data object Pending : ImmutableMessagingIndicator
    @Immutable
    data object Generating : ImmutableMessagingIndicator
    @Immutable
    data class DeterminedGenerating(val progress: Float) : ImmutableMessagingIndicator
}