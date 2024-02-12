package ru.kima.intelligentchat.core.preferences

import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.core.common.API_TYPE

@Serializable
data class AppPreferences(
    val selectedPersonaId: Long = 0,
    val selectedApiType: API_TYPE = API_TYPE.HORDE
)
