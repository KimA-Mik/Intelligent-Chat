package ru.kima.intelligentchat.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val root: String
) {
    Characters(
        title = "Characters",
        selectedIcon = Icons.Filled.Article,
        unselectedIcon = Icons.Outlined.Article,
        root = "cardsScreen"
    ),
    Personas(
        title = "Personas",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        root = "personasScreen"
    )
}