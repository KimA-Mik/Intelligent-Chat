package ru.kima.intelligentchat.data.kobold.horde.model

import kotlinx.coroutines.flow.MutableStateFlow

object ConnectionState {
    val isConnected = MutableStateFlow(false)
}