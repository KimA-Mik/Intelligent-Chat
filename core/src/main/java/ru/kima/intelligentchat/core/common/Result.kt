package ru.kima.intelligentchat.core.common

sealed interface Result<T, E : Throwable> {
    data class Success<T, E : Throwable>(val data: T) : Result<T, E>
    data class Error<T, E : Throwable>(val cause: E) : Result<T, E>
}