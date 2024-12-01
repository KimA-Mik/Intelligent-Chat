package ru.kima.intelligentchat.presentation.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ru.kima.intelligentchat.presentation.android.preferences.appAppearance.isNightMode
import ru.kima.intelligentchat.presentation.ui.theme.colorScheme.MonetColorScheme
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
    darkTheme: Boolean = isNightMode(),
    isAmoled: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            MonetColorScheme(context)
        }

        else -> TidalWaveColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme.getColorScheme(isDark = darkTheme, isAmoled = isAmoled),
        typography = Typography,
        content = content
    )
}