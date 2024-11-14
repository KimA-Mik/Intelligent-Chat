package ru.kima.intelligentchat.domain.common.errors

import java.text.SimpleDateFormat

object Formatter {
    fun defaultFormat(timeMs: Long): String {
        val formatter = SimpleDateFormat.getDateTimeInstance()
        return formatter.format(timeMs)
    }
}