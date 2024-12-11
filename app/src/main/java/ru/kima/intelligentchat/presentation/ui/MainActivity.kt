package ru.kima.intelligentchat.presentation.ui

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.core.util.Consumer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.android.get
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.android.preferences.appAppearance.AppAppearanceStore
import ru.kima.intelligentchat.presentation.navigation.model.APP_BASE_URL
import ru.kima.intelligentchat.presentation.navigation.model.APP_SCHEMA
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

class MainActivity : ComponentActivity() {
    private val applicationEvent = MutableStateFlow(Event<ApplicationEvent>(null))

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            DisposableEffect(Unit) {
                val listener = Consumer<Intent> { intent ->
                    intent.data?.let { uri ->
                        val notificationId = intent.getIntExtra(NOTIFICATION_ID_KEY, 0)
                        applicationEvent.value =
                            Event(ApplicationEvent.NotificationNavigation(uri, notificationId))
                    }
                }
                addOnNewIntentListener(listener)
                onDispose { removeOnNewIntentListener(listener) }
            }
            val appearanceStore = remember { get<AppAppearanceStore>() }
            val appearance by appearanceStore.appAppearance().collectAsStateWithLifecycle()

            IntelligentChatTheme(
                isAmoled = appearance.darkModePureBlack,
                dynamicColor = true,
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val windowSizeClass = calculateWindowSizeClass(this)
                    val event by applicationEvent.collectAsStateWithLifecycle()
                    ApplicationScreen(
                        windowSizeClass = windowSizeClass,
                        applicationEvent = event
                    )
                }
            }
        }
    }

    companion object {
        fun getOpenChatIntent(context: Context, cardId: Long, notificationId: Int): PendingIntent {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "$APP_SCHEMA:$APP_BASE_URL/chat/$cardId".toUri(),
                context,
                MainActivity::class.java
            )
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra(NOTIFICATION_ID_KEY, notificationId)

            return PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        private const val NOTIFICATION_ID_KEY = "notification_id"
    }
}
