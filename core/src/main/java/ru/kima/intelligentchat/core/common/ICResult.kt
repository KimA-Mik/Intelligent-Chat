package ru.kima.intelligentchat.core.common

sealed interface ICResult<S, E> {
    data class Success<S, E>(val data: S) : ICResult<S, E>
    data class Error<S, E>(val data: E) : ICResult<S, E>
}