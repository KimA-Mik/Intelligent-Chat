package ru.kima.intelligentchat.domain.preferences.chatSettings

import ru.kima.intelligentchat.domain.preferences.Preference

interface ChatSettingsRepository {
    fun showDate(): Preference<Boolean>
    fun showNumber(): Preference<Boolean>
}