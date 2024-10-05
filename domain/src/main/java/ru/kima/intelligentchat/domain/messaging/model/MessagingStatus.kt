package ru.kima.intelligentchat.domain.messaging.model

sealed interface MessagingStatus {
    data object Available : MessagingStatus
    data object Pending : MessagingStatus
    data object Generating : MessagingStatus
    data class GeneratingWithProgress(val progression: Float) : MessagingStatus
}