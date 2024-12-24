package ru.kima.intelligentchat.util.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import ru.kima.intelligentchat.presentation.android.preferences.appAppearance.isNightMode
import ru.kima.intelligentchat.presentation.ui.LocalNavController
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun ICPreview(
    darkTheme: Boolean = isNightMode(),
    isAmoled: Boolean = false,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalNavController provides rememberNavController()
    ) {
        IntelligentChatTheme(
            darkTheme = darkTheme,
            isAmoled = isAmoled,
            dynamicColor = dynamicColor,
        ) {
            Surface {
                content()
            }
        }
    }
}