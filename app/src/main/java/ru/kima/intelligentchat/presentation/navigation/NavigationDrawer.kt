package ru.kima.intelligentchat.presentation.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.presentation.ui.MainActivityViewModel


@Composable
fun NavigationDrawer(
    drawerSelected: Int,
    drawerState: DrawerState,
    navController: NavHostController,
    navBackStackEntry: NavBackStackEntry,
    onEvent: (MainActivityViewModel.UserEvent) -> Unit,
    content: @Composable () -> Unit
) {
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
                    navController.navigateFromRoot(navBackStackEntry, navItem.root)
                })
        },
        content = content
    )
}

@Composable
fun NavigationDrawerContent(
    selectedIndex: Int = 0,
    onSelect: (Int) -> Unit
) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        NavItem.entries.forEachIndexed { index, navItem ->
            val selected = selectedIndex == index
            NavigationDrawerItem(
                label = {
                    Text(text = navItem.title)
                },
                selected = selected,
                onClick = { onSelect(index) },
                icon = {
                    Icon(
                        imageVector = if (selected) navItem.selectedIcon else navItem.unselectedIcon,
                        contentDescription = navItem.title
                    )
                },
                modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}