package ru.kima.intelligentchat.domain.messaging.util

fun String.inlinePersonaName(name: String) = replace(PERSONA_NAME_PLACEHOLDER, name, true)
fun String.inlineCardName(name: String) = replace(CARD_NAME_PLACEHOLDER, name, true)