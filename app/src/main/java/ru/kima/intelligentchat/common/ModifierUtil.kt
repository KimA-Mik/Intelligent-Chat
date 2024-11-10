package ru.kima.intelligentchat.common

import androidx.compose.ui.Modifier

inline fun Modifier.conditional(predicate: Boolean, action: Modifier.() -> Modifier): Modifier {
    return if (predicate) action()
    else this
}