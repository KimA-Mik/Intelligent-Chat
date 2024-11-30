package ru.kima.intelligentchat.presentation.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableList
import ru.kima.intelligentchat.presentation.common.components.AppBar
import ru.kima.intelligentchat.presentation.settings.screen.components.SettingsListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceScaffold(
    @StringRes
    titleRes: Int,
    actions: @Composable RowScope.() -> Unit = {},
    onBackPressed: (() -> Unit)? = null,
    itemsProvider: @Composable () -> ImmutableList<Setting>,
) {
    val sb = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            AppBar(
                titleContent = { Text(text = stringResource(titleRes)) },
                navigateUp = onBackPressed,
                actions = actions,
                scrollBehavior = sb,
            )
        },
        content = { contentPadding ->
            SettingsListScreen(
                settings = itemsProvider(),
                modifier = Modifier
                    .padding(contentPadding)
                    .nestedScroll(sb.nestedScrollConnection),
            )
        },
    )
}