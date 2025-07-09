package ru.kima.intelligentchat.presentation.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.android.notifications.NotificationHandler
import ru.kima.intelligentchat.presentation.navigation.ChatNavHost

val LocalNavController = compositionLocalOf<NavController> {
    error("CompositionLocal LocalNavController not presented")
}

@Composable
fun ApplicationScreen(
    windowSizeClass: WindowSizeClass,
    applicationEvent: Event<ApplicationEvent>
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    val expanded = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val navigationHandler = koinInject<NotificationHandler>()
    LaunchedEffect(applicationEvent) {
        applicationEvent.consume { event ->
            when (event) {
                is ApplicationEvent.NotificationNavigation -> {
                    if (event.notificationId != 0) {
                        navigationHandler.cancelNotification(event.notificationId)
                    }
                    navController.navigate(event.deeplink)
                }
            }
        }
    }

    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        ChatNavHost(
            navController = navController,
            snackbarHostState = snackbarHostState,
            drawerState = drawerState,
            expanded = expanded
        )
    }
}