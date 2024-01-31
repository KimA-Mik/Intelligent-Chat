package ru.kima.intelligentchat.data.common

import androidx.room.TypeConverter

class IntListConverter {
    @TypeConverter
    fun fromString(value: String): List<Int> {
        return value
            .split(',')
            .map { it.toInt() }
    }

    @TypeConverter
    fun intArrayToString(intList: List<Int>): String {
        return intList.joinToString(separator = ",")
    }
}