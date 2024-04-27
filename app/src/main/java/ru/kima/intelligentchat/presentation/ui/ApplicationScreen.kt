package ru.kima.intelligentchat.presentation.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI
import ru.kima.intelligentchat.presentation.navigation.ChatNavHost

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ApplicationScreen(windowSizeClass: WindowSizeClass) {
    KoinAndroidContext {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val snackbarHostState = remember { SnackbarHostState() }

        val extended = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

        ChatNavHost(
            navController = navController,
            snackbarHostState = snackbarHostState,
            drawerState = drawerState,
            extended = extended
        )
    }
}