package ru.kima.intelligentchat.presentation.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawerContent(
    selectedIndex: Int = 0,
    onSelect: (Int) -> Unit
) = ModalDrawerSheet {
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