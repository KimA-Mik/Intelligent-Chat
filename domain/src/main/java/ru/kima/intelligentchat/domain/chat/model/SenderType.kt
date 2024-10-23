package ru.kima.intelligentchat.domain.chat.model

enum class SenderType {
    Character,
    Persona;

    companion object {
        fun fromString(string: String): SenderType? {
            return try {
                valueOf(string)
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}