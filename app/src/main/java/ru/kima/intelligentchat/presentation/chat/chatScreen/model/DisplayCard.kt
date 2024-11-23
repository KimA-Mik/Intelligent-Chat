package ru.kima.intelligentchat.presentation.chat.chatScreen.model

import androidx.compose.runtime.Immutable

@Immutable
data class DisplayCard(
    val id: Long = 0L,
    val name: String = String(),
    val photoName: String? = null
)
