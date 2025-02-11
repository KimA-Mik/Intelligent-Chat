package ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.presentation.common.components.AppBar
import ru.kima.intelligentchat.presentation.ui.LocalNavController
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropdownMenu
import ru.kima.intelligentchat.util.preview.ICPreview

@Composable
fun ContextTemplateRoot() {
    ContextTemplateScreen(
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContextTemplateScreen(modifier: Modifier = Modifier) {
    val navController = LocalNavController.current
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                titleContent = {
                    Text(text = stringResource(R.string.context_template_setting_title))
                },
                navigateUp = { navController.popBackStack() },
                actions = {
                    SimpleDropdownMenu(
                        menuItems = dropdownMenuItems(),
                        iconVector = Icons.Default.MoreVert
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {

        }
    }
}

@Composable
private fun dropdownMenuItems() = remember {
    listOf(
        SimpleDropDownMenuItem(
            string = ComposeString.Resource(R.string.menu_item_rename_template),
            onClick = {},
            iconVector = Icons.Default.Edit
        ),
        SimpleDropDownMenuItem(
            string = ComposeString.Resource(R.string.menu_item_update_current_template),
            onClick = {},
            iconVector = Icons.Default.Save
        ),
        SimpleDropDownMenuItem(
            string = ComposeString.Resource(R.string.manu_item_save_template_as),
            onClick = {},
            iconVector = Icons.Default.SaveAs
        ),
        SimpleDropDownMenuItem(
            string = ComposeString.Resource(R.string.menu_item_delete_template),
            onClick = {},
            iconVector = Icons.Default.DeleteForever
        )
    )
}


@Preview
@Composable
private fun ContextTemplateScreenPreview() = ICPreview {
    ContextTemplateScreen(modifier = Modifier.fillMaxSize())
}