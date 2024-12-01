package ru.kima.intelligentchat.presentation.ui.theme.colorScheme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

abstract class ICColorScheme {
    abstract val darkScheme: ColorScheme
    abstract val lightScheme: ColorScheme

    fun getColorScheme(
        isDark: Boolean,
        isAmoled: Boolean
    ): ColorScheme {
        if (!isDark) return lightScheme

        if (!isAmoled) return darkScheme


        return darkScheme.copy(
            background = Color.Black
        )
    }
}