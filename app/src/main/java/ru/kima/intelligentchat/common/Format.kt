package ru.kima.intelligentchat.common

fun Double.format(length: Int): String {
    return String.format(
        "%.${length}f",
        this
    )
}

fun Double.formatAndTrim(length: Int): String {
    return String.format(
        "%.${length}f",
        this
    )
        .trimEnd('0')
        .trimEnd('.', ',')
}

fun Float.format(length: Int): String {
    return String.format(
        "%.${length}f",
        this
    )
}

fun Float.formatAndTrim(length: Int): String {
    return String.format(
        "%.${length}f",
        this
    )
        .trimEnd('0')
        .trimEnd('.', ',')
}