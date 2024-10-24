package ru.kima.intelligentchat.core.common


sealed interface ICResult<out S, out E> {
    data class Success<out S, E>(val data: S) : ICResult<S, E>
    data class Error<S, out E>(val data: E) : ICResult<S, E>
}

inline fun <S, E> ICResult<S, E>.valueOr(alternative: (E) -> S): S {
    return when (this) {
        is ICResult.Error -> alternative(data)
        is ICResult.Success -> data
    }
}