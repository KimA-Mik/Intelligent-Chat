package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationLayout
import ru.kima.intelligentchat.presentation.settings.root.SettingsRoot

fun NavGraphBuilder.settingsGraph(
    expanded: Boolean,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState
) {
    navigation(startDestination = "root", route = NavItem.Settings.root) {
        composable("root") {
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
    }
}