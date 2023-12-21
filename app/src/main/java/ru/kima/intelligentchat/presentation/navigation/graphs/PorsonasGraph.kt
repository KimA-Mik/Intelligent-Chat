package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationDrawer
import ru.kima.intelligentchat.presentation.personas.list.PersonaListScreen

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
                PersonaListScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    drawerState = drawerState
                )
            }
        }
    }
}

