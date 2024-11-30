package ru.kima.intelligentchat.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ru.kima.intelligentchat.presentation.android.preferences.appAppearance.AppAppearance
import ru.kima.intelligentchat.presentation.ui.theme.colorScheme.TidalWaveColorScheme

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun IntelligentChatTheme(
    darkTheme: AppAppearance.DarkMode = AppAppearance.DarkMode.SYSTEM,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val actualDarkTheme = when (darkTheme) {
        AppAppearance.DarkMode.SYSTEM -> isSystemInDarkTheme()
        AppAppearance.DarkMode.ON -> true
        AppAppearance.DarkMode.OFF -> false
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (actualDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                context
            )
        }

//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
        actualDarkTheme -> TidalWaveColorScheme.darkScheme
        else -> TidalWaveColorScheme.lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}