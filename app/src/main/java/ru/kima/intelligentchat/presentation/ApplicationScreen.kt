package ru.kima.intelligentchat.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI
import ru.kima.intelligentchat.presentation.navigation.ChatNavHost

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ApplicationScreen(
) {
    KoinAndroidContext {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        ChatNavHost(navController = navController, snackbarHostState = snackbarHostState)
    }
}