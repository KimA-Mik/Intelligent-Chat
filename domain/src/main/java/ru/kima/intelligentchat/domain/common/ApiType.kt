package ru.kima.intelligentchat.domain.common

enum class ApiType {
    KOBOLD_AI,
    HORDE;

    companion object {
        fun fromString(string: String): ApiType? {
            return try {
                valueOf(string)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}