package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.presentation.connection.overview.ConnectionOverviewScreen
import ru.kima.intelligentchat.presentation.connection.overview.ConnectionOverviewViewModel
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent
import ru.kima.intelligentchat.presentation.connection.presets.horde.edit.HordePresetEditScreen
import ru.kima.intelligentchat.presentation.connection.presets.horde.edit.HordePresetEditScreenState
import ru.kima.intelligentchat.presentation.connection.presets.horde.edit.HordePresetEditViewModel
import ru.kima.intelligentchat.presentation.connection.presets.horde.edit.events.UserEvent
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationDrawer

fun NavGraphBuilder.connectionGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
) {
    navigation(startDestination = "overview", route = NavItem.Connection.root) {
        composable("overview") {
            val viewModel: ConnectionOverviewViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            val uiEvents by viewModel.uiEvents.collectAsState()
            val onEvent = remember<(COUserEvent) -> Unit> {
                {
                    viewModel.onEvent(it)
                }
            }

            NavigationDrawer(
                drawerState = drawerState,
                navController = navController
            ) {
                ConnectionOverviewScreen(
                    state = state,
                    uiEvents = uiEvents,
                    drawerState = drawerState,
                    snackbarHostState = snackbarHostState,
                    navController = navController,
                    onEvent = onEvent
                )
            }
        }

        composable("presets/{presetId}") {
            val viewModel: HordePresetEditViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle(HordePresetEditScreenState.NoPreset)
            val onEvent = remember<(UserEvent) -> Unit> {
                {
                    viewModel.onEvent(it)
                }
            }

            HordePresetEditScreen(
                navController = navController,
                state = state,
                onEvent = onEvent
            )
        }
    }
}

fun NavController.navigateToHordePreset(presetId: Long) =
    this.navigate("presets/${presetId}") { launchSingleTop = true }
