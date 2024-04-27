package ru.kima.intelligentchat.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun IntelligentChatNavigationRail(
    navController: NavHostController,
) {
    NavigationRail {
        NavigationRailContent(navController = navController)
    }
}

@Composable
fun NavigationRailContent(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        NavItem.entries.forEach { navItem ->
            val selected = currentDestination?.hierarchy?.any { it.route == navItem.root } == true
            val title = stringResource(id = navItem.titleId)

            NavigationRailItem(
                selected = selected,
                onClick = {
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
                        contentDescription = title
                    )
                },
                label = {
                    Text(text = stringResource(navItem.titleId))
                })
        }
    }
}