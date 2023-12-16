package ru.kima.intelligentchat.presentation.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.CardDetailsScreen
import ru.kima.intelligentchat.presentation.characterCard.charactersList.CharactersListScreen
import ru.kima.intelligentchat.presentation.navigation.NavItem
import ru.kima.intelligentchat.presentation.navigation.NavigationDrawerContent
import ru.kima.intelligentchat.presentation.navigation.navigateFromRoot
import ru.kima.intelligentchat.presentation.ui.MainActivityViewModel

fun NavGraphBuilder.cardGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
    drawerSelected: Int,
    onEvent: (MainActivityViewModel.UserEvent) -> Unit
) {
    navigation(startDestination = "cards", route = NavItem.Characters.root) {
        composable("cards") {
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
                CharactersListScreen(
                    navController,
                    snackbarHostState,
                    drawerState,
                    koinInject(),
                    koinInject(),
                    koinViewModel()
                )
            }
        }
        composable(
            route = "cards/{cardId}",
            arguments = listOf(
                navArgument(name = "cardId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            CardDetailsScreen(navController, snackbarHostState, koinInject(), koinViewModel())
        }
    }
}

fun NavController.navigateToCard(cardId: Long) =
    this.navigate("cards/${cardId}") { launchSingleTop = true }
