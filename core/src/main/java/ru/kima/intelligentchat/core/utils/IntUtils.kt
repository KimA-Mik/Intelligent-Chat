package ru.kima.intelligentchat.core.utils

fun Int.clipIntToRange(lhs: Int, rhs: Int): Int {
    return when {
        this < lhs -> lhs
        this > rhs -> rhs
        else -> this
    }
}
