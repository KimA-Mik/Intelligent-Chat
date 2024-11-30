package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationLayout
import ru.kima.intelligentchat.presentation.settings.root.SettingsRoot
import ru.kima.intelligentchat.presentation.settings.settingsScreens.chat.ChatSettingsRoot

private const val ROOT_DESTINATION = "root"
private const val CHAT_SETTINGS_DESTINATION = "chatSettings"

fun NavGraphBuilder.settingsGraph(
    expanded: Boolean,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState
) {
    navigation(startDestination = ROOT_DESTINATION, route = NavItem.Settings.root) {
        composable(ROOT_DESTINATION) {
            NavigationLayout(
                expanded = expanded,
                navController = navController,
                drawerState = drawerState
            ) {
                SettingsRoot(
                    expanded = expanded,
                    drawerState = drawerState,
                    navController = navController,
                    snackbarHostState = snackbarHostState
                )
            }
        }

        composable(CHAT_SETTINGS_DESTINATION) {
            ChatSettingsRoot(
                navController = navController,
                snackbarHostState = snackbarHostState
            )
        }
    }
}

fun NavController.navigateToChatSettings() =
    navigate(CHAT_SETTINGS_DESTINATION)