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
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.InstructModeTemplateRoot

private const val ROOT_DESTINATION = "root"
private const val INSTRUCT_MODE_DESTINATION = "instruct_mode"

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

        composable(INSTRUCT_MODE_DESTINATION) {
            InstructModeTemplateRoot()
        }
    }
}

fun NavController.navigateToInstructMode() = navigate(INSTRUCT_MODE_DESTINATION)