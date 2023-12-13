package ru.kima.intelligentchat.domain.persona.model

import android.graphics.Bitmap

data class Persona(
    val id: Long = 0,
    val name: String = String(),
    val description: String = String(),
    val bitmap: Bitmap? = null,
    val personaWordsCount: Long = 0,
    val charactersWordsCount: Long = 0,
    val personaMessages: Long = 0,
    val charactersMessages: Long = 0,
    val swipes: Long = 0
)
