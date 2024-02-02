package ru.kima.intelligentchat.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Contactless
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import ru.kima.intelligentchat.R

enum class NavItem(
    val titleId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val root: String
) {
    Characters(
        titleId = R.string.nav_item_characters,
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        unselectedIcon = Icons.AutoMirrored.Outlined.Article,
        root = "cardsScreen"
    ),
    Personas(
        titleId = R.string.nav_item_personas,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        root = "personasScreen"
    ),
    Connection(
        titleId = R.string.nav_item_connection,
        selectedIcon = Icons.Filled.Contactless,
        unselectedIcon = Icons.Outlined.Contactless,
        root = "connection"
    )
}