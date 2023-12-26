package ru.kima.intelligentchat.domain.persona.model

data class Persona(
    val id: Long = 0,
    val name: String = String(),
    val description: String = String(),
    val imageName: String? = null,
    val personaWordsCount: Long = 0,
    val charactersWordsCount: Long = 0,
    val personaMessages: Long = 0,
    val charactersMessages: Long = 0,
    val swipes: Long = 0
)
