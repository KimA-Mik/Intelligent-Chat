package ru.kima.intelligentchat.domain.preferences.app

import ru.kima.intelligentchat.domain.common.ApiType

data class AppPreferences(
    val selectedPersonaId: Long,
    val selectedApiType: ApiType,
    val generationPending: Boolean
)