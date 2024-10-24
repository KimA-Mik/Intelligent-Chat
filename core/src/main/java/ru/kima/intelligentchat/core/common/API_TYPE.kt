package ru.kima.intelligentchat.core.common

enum class API_TYPE {
    KOBOLD_AI,
    HORDE;

    companion object {
        fun fromString(string: String): API_TYPE? {
            return try {
                valueOf(string)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}