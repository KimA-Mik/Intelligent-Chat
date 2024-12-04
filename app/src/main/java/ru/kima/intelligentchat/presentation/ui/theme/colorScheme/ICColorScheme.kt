package ru.kima.intelligentchat.presentation.ui.theme.colorScheme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

abstract class ICColorScheme {
    abstract val darkScheme: ColorScheme
    abstract val lightScheme: ColorScheme

    private val surfaceContainer = Color(0xFF0C0C0C)
    private val surfaceContainerHigh = Color(0xFF131313)
    private val surfaceContainerHighest = Color(0xFF1B1B1B)

    fun getColorScheme(
        isDark: Boolean,
        isAmoled: Boolean
    ): ColorScheme {
        if (!isDark) return lightScheme

        if (!isAmoled) return darkScheme

        return darkScheme.copy(
            background = Color.Black,
            onBackground = Color.White,
            surface = Color.Black,
            onSurface = Color.White,
            surfaceVariant = surfaceContainer,
            surfaceContainerLowest = surfaceContainer,
            surfaceContainerLow = surfaceContainer,
            surfaceContainer = surfaceContainer, // Navigation bar background
            surfaceContainerHigh = surfaceContainerHigh,
            surfaceContainerHighest = surfaceContainerHighest,
        )
    }
}