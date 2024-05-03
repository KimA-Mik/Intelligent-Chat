package ru.kima.intelligentchat.presentation.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun NavigationLayout(
    expanded: Boolean,
    navController: NavHostController,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    when (expanded) {
        true -> Row {
            IntelligentChatNavigationRail(navController)
            content()
        }

        false -> NavigationDrawer(
            drawerState = drawerState,
            navController = navController,
        ) {
            content()
        }
    }
}