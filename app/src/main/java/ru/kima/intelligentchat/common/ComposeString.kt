package ru.kima.intelligentchat.common

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface ComposeString {
    data class Raw(val value: String) : ComposeString
    data class Resource(@StringRes val value: Int) : ComposeString

    @Composable
    fun unwrap(): String {
        return when (this) {
            is Raw -> value
            is Resource -> stringResource(value)
        }
    }

    companion object {
        val Empty = Raw("")
    }
}