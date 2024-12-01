package ru.kima.intelligentchat.presentation.android.preferences.appAppearance

import android.app.UiModeManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun isNightMode() = when (AppCompatDelegate.getDefaultNightMode()) {
    AppCompatDelegate.MODE_NIGHT_NO -> false
    AppCompatDelegate.MODE_NIGHT_YES -> true
    else -> isSystemInDarkTheme()
}

fun UiModeManager.setDarkMode(darkMode: AppAppearance.DarkMode) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        setApplicationNightMode(
            when (darkMode) {
                AppAppearance.DarkMode.SYSTEM -> UiModeManager.MODE_NIGHT_AUTO
                AppAppearance.DarkMode.ON -> UiModeManager.MODE_NIGHT_YES
                AppAppearance.DarkMode.OFF -> UiModeManager.MODE_NIGHT_NO
            }
        )
    } else {
        AppCompatDelegate.setDefaultNightMode(
            when (darkMode) {
                AppAppearance.DarkMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                AppAppearance.DarkMode.ON -> AppCompatDelegate.MODE_NIGHT_YES
                AppAppearance.DarkMode.OFF -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

@Composable
fun UiModeManager.isNightMode(): Boolean {
    return when (nightMode) {
        UiModeManager.MODE_NIGHT_YES -> true
        UiModeManager.MODE_NIGHT_NO -> false
        else -> isSystemInDarkTheme()
    }
}
