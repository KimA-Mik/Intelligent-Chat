package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import ru.kima.intelligentchat.presentation.navigation.NavigationLayout
import ru.kima.intelligentchat.presentation.personas.details.PersonaDetailsScreen
import ru.kima.intelligentchat.presentation.personas.details.PersonaDetailsViewModel
import ru.kima.intelligentchat.presentation.personas.list.PersonaListScreen
import ru.kima.intelligentchat.presentation.personas.list.PersonasListViewModel

fun NavGraphBuilder.personasGraph(
    expanded: Boolean,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
) {
    navigation(startDestination = "personas", route = NavItem.Personas.root) {
        composable("personas") {
            NavigationLayout(
                expanded = expanded,
                drawerState = drawerState,
                navController = navController,
            ) {
                val viewModel: PersonasListViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                val onEvent =
                    remember<(ru.kima.intelligentchat.presentation.personas.list.events.UserEvent) -> Unit> {
                        {
                            viewModel.onEvent(it)
                        }
                    }

                PersonaListScreen(
                    expanded = expanded,
                    state = state,
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    drawerState = drawerState,
                    events = viewModel.events,
                    onEvent = onEvent
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
            val onEvent =
                remember<(ru.kima.intelligentchat.presentation.personas.details.events.UserEvent) -> Unit> {
                    {
                        viewModel.onEvent(it)
                    }
                }

            PersonaDetailsScreen(
                navController = navController,
                snackbarHostState = snackbarHostState,
                imagePicker = koinInject(),
                state = state,
                uiEvents = viewModel.uiEvents,
                onEvent = onEvent
            )
        }
    }
}

fun NavController.navigateToPersona(personaId: Long) =
    this.navigate("personas/${personaId}") { launchSingleTop = true }

