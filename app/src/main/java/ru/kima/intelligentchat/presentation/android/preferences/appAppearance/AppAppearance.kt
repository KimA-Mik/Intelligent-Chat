package ru.kima.intelligentchat.presentation.android.preferences.appAppearance

data class AppAppearance(
    val darkMode: DarkMode
) {

    enum class DarkMode {
        SYSTEM, ON, OFF
    }
}
