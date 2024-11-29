package ru.kima.intelligentchat.domain.preferences.chatSettings

import ru.kima.intelligentchat.domain.preferences.Preference

interface ChatSettingsRepository {
    val showDate: Preference<Boolean>
    val showNumber: Preference<Boolean>
}