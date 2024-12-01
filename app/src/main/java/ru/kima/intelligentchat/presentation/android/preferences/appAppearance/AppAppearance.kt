package ru.kima.intelligentchat.presentation.android.preferences.appAppearance

data class AppAppearance(
    val darkMode: DarkMode,
    val darkModePureBlack: Boolean,
) {

    enum class DarkMode {
        SYSTEM, ON, OFF
    }
}
