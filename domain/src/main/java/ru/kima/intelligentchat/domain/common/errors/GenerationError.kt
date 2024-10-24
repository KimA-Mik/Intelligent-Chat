package ru.kima.intelligentchat.domain.common.errors

sealed interface GenerationError {
    data object NotImplemented : GenerationError
}