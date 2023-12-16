package ru.kima.intelligentchat.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntelligentChatTheme(dynamicColor = true) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val selected by viewModel.selectedNavigationDrawerItem.collectAsState()
                    ApplicationScreen(
                        selected = selected,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}
