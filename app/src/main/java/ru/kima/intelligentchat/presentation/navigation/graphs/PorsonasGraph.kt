package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationDrawer
import ru.kima.intelligentchat.presentation.personas.details.PersonaDetailsScreen
import ru.kima.intelligentchat.presentation.personas.details.PersonaDetailsViewModel
import ru.kima.intelligentchat.presentation.personas.list.PersonaListScreen
import ru.kima.intelligentchat.presentation.personas.list.PersonasListViewModel

fun NavGraphBuilder.personasGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
) {
    navigation(startDestination = "personas", route = NavItem.Personas.root) {
        composable("personas") {
            NavigationDrawer(
                drawerState = drawerState,
                navController = navController,
            ) {
                val viewModel: PersonasListViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                PersonaListScreen(
                    state = state,
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    drawerState = drawerState,
                    events = viewModel.events,
                    onEvent = viewModel::onEvent
                )
            }
        }
        composable(
            route = "personas/{personaId}",
            arguments = listOf(
                navArgument(name = "personaId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val viewModel: PersonaDetailsViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            PersonaDetailsScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                imagePicker = koinInject(),
                state = state,
                onEvent = viewModel::onEvent
            )
        }
    }
}

fun NavController.navigateToPersona(personaId: Long) =
    this.navigate("personas/${personaId}") { launchSingleTop = true }

