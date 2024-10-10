package ru.kima.intelligentchat.data.kobold.horde.model

import kotlinx.coroutines.flow.MutableStateFlow

object HordeConnectionState {
    val isConnected = MutableStateFlow(false)
}