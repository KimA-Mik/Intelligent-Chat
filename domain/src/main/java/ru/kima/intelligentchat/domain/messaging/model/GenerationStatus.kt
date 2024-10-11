package ru.kima.intelligentchat.domain.messaging.model

sealed interface GenerationStatus {
    data object None : GenerationStatus
    data object Pending : GenerationStatus
    data object Generating : GenerationStatus
    data class GeneratingWithProgress(val progression: Float) : GenerationStatus
}