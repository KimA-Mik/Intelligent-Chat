package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.presentation.connection.overview.ConnectionOverviewScreen
import ru.kima.intelligentchat.presentation.connection.overview.ConnectionOverviewViewModel
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent
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
            val onEvent = remember<(COUserEvent) -> Unit> {
                {
                    viewModel.onEvent(it)
                }
            }

            NavigationDrawer(
                drawerState = drawerState,
                navController = navController
            ) {
                ConnectionOverviewScreen(state = state, onEvent = onEvent)
            }
        }
    }
}