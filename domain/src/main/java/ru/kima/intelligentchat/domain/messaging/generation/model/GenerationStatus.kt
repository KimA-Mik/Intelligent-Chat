package ru.kima.intelligentchat.domain.messaging.generation.model

import ru.kima.intelligentchat.domain.common.errors.GenerationError

sealed interface GenerationStatus {
    data class Started(val generationId: String) : GenerationStatus
    data class Error(val error: GenerationError) : GenerationStatus
    data object Pending : GenerationStatus
    data object Generating : GenerationStatus
    data class GeneratingWithProgress(val progression: Float) : GenerationStatus
    data class Done(val result: String) : GenerationStatus
    data object Aborted : GenerationStatus
}