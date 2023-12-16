package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationDrawerContent
import ru.kima.intelligentchat.presentation.navigation.navigateFromRoot
import ru.kima.intelligentchat.presentation.personas.list.PersonaListScreen
import ru.kima.intelligentchat.presentation.ui.MainActivityViewModel

fun NavGraphBuilder.personasGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
    drawerSelected: Int,
    onEvent: (MainActivityViewModel.UserEvent) -> Unit
) {
    navigation(startDestination = "personas", route = NavItem.Personas.root) {
        composable("personas") {
            val scope = rememberCoroutineScope()
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    NavigationDrawerContent(selectedIndex = drawerSelected,
                        onSelect = { index ->
                            scope.launch {
                                drawerState.close()
                            }
                            onEvent(MainActivityViewModel.UserEvent.SelectNavigationDrawerItem(index))
                            val navItem = NavItem.entries[index]
                            navController.navigateFromRoot(it, navItem.root)
                        })
                }) {
                PersonaListScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    drawerState = drawerState
                )
            }
        }
    }
}
