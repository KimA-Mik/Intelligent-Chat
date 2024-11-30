package ru.kima.intelligentchat.presentation.settings.settingsScreens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ImmutableChatAppearance
import ru.kima.intelligentchat.presentation.common.components.AppBar
import ru.kima.intelligentchat.presentation.settings.screen.components.SwitchSettingsItem
import ru.kima.intelligentchat.presentation.settings.settingsScreens.chat.events.ChatSettingsAction
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun ChatSettingsRoot(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: ChatSettingsViewModel = koinViewModel()
) {
    val appearance by viewModel.chatAppearance.collectAsStateWithLifecycle(ImmutableChatAppearance.default)
    val onEvent = remember<(ChatSettingsAction) -> Unit> {
        {
            viewModel.onEvent(it)
        }
    }

    ChatSettingsScreen(
        appearance = appearance,
        onEvent = onEvent,
        navController = navController,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSettingsScreen(
    appearance: ImmutableChatAppearance,
    onEvent: (ChatSettingsAction) -> Unit,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            AppBar(
                titleContent = {
                    Text(text = stringResource(R.string.settings_nav_item_chat_settings_title))
                },
                navigateUp = { navController.navigateUp() },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        ChatSettingsContent(
            appearance = appearance,
            onEvent = onEvent,
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}

@Composable
fun ChatSettingsContent(
    appearance: ImmutableChatAppearance,
    onEvent: (ChatSettingsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        SwitchSettingsItem(
            title = stringResource(R.string.setting_show_message_number_title),
            checked = appearance.showNumber,
            onCheckedChange = { onEvent(ChatSettingsAction.SetShowNumber(it)) },
            modifier = Modifier,
        )

        SwitchSettingsItem(
            title = stringResource(R.string.setting_show_swipe_date_title),
            checked = appearance.showDate,
            onCheckedChange = { onEvent(ChatSettingsAction.SetShowDate(it)) }
        )
    }
}

@Preview
@Composable
private fun ChatSettingsPreview() {
    IntelligentChatTheme {
        Surface {
            ChatSettingsScreen(
                appearance = ImmutableChatAppearance.default,
                onEvent = {},
                navController = rememberNavController(),
                snackbarHostState = SnackbarHostState()
            )
        }
    }
}