package ru.kima.intelligentchat.domain.messaging.model

sealed interface MessagingIndicator {
    data object None : MessagingIndicator
    data object Pending : MessagingIndicator
    data object Generating : MessagingIndicator
    data class DeterminedGenerating(val progress: Float) : MessagingIndicator
}