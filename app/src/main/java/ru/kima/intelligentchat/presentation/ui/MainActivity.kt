package ru.kima.intelligentchat.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.android.ext.android.get
import ru.kima.intelligentchat.presentation.android.preferences.appAppearance.AppAppearanceStore
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val appearanceStore = remember { get<AppAppearanceStore>() }
            val appearance by appearanceStore.appAppearance().collectAsStateWithLifecycle()
            IntelligentChatTheme(
                darkTheme = appearance.darkMode,
                dynamicColor = true
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val windowSizeClass = calculateWindowSizeClass(this)

                    ApplicationScreen(windowSizeClass)
                }
            }
        }
    }
}
