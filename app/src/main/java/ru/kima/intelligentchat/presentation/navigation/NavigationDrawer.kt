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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch


@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    navController: NavHostController,
//    navBackStackEntry: NavBackStackEntry,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(drawerState = drawerState, navController = navController)
        },
        content = content
    )
}

@Composable
fun NavigationDrawerContent(
    drawerState: DrawerState,
    navController: NavHostController,

    ) {
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        NavItem.entries.forEach { navItem ->
            val selected = currentDestination?.hierarchy?.any { it.route == navItem.root } == true
            NavigationDrawerItem(
                label = {
                    Text(text = navItem.title)
                },
                selected = selected,
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }

                    //Example from: https://developer.android.com/jetpack/compose/navigation#bottom-nav
                    navController.navigate(navItem.root) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
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