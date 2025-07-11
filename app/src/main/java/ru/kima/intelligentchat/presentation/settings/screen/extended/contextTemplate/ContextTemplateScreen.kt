package ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate
import ru.kima.intelligentchat.presentation.common.components.AppBar
import ru.kima.intelligentchat.presentation.common.components.clearFocusOnSoftKeyboardHide
import ru.kima.intelligentchat.presentation.common.dialogs.SimpleAlertDialog
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.events.UserEvent
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.model.DisplayContextTemplate
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.model.toDisplay
import ru.kima.intelligentchat.presentation.ui.LocalNavController
import ru.kima.intelligentchat.presentation.ui.components.DropdownTextField
import ru.kima.intelligentchat.presentation.ui.components.EditTextDialog
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropdownMenu
import ru.kima.intelligentchat.presentation.ui.components.TooltipIconButton
import ru.kima.intelligentchat.util.preview.ICPreview

private typealias OnEvent = (UserEvent) -> Unit

@Composable
fun ContextTemplateRoot() {
    val viewModel: ContextTemplateViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = remember<OnEvent> {
        {
            viewModel.onEvent(it)
        }
    }

    ContextTemplateScreen(
        state = state,
        onEvent = onEvent,
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContextTemplateScreen(
    state: ContextTemplateScreenState,
    onEvent: OnEvent,
    modifier: Modifier = Modifier
) {
    when {
        state.renameDialog -> EditTextDialog(
            value = state.dialogBuffer,
            onValueChange = { onEvent(UserEvent.UpdateDialogBuffer(it)) },
            onAccept = { onEvent(UserEvent.AcceptRenameTemplateDialog) },
            onDismiss = { onEvent(UserEvent.DismissRenameTemplateDialog) },
            icon = Icons.Default.Edit,
            textFieldLabel = remember { ComposeString.Resource(R.string.rename_template_dialog_input_label) }
        )

        state.saveAsDialog -> EditTextDialog(
            value = state.dialogBuffer,
            onValueChange = { onEvent(UserEvent.UpdateDialogBuffer(it)) },
            onAccept = { onEvent(UserEvent.AcceptSaveAsDialog) },
            onDismiss = { onEvent(UserEvent.DismissSaveAsDialog) },
            icon = Icons.Default.SaveAs,
            textFieldLabel = remember { ComposeString.Resource(R.string.rename_template_dialog_input_label) }
        )

        state.deleteDialog -> SimpleAlertDialog(
            onConfirm = { onEvent(UserEvent.AcceptDeleteTemplateDialog) },
            onDismiss = { onEvent(UserEvent.DismissDeleteTemplateDialog) },
            title = stringResource(R.string.alert_dialog_title_delete_template),
            text = stringResource(
                R.string.alert_dialog_text_delete_context_template,
                state.currentTemplate.name
            ),
            icon = Icons.Default.DeleteForever,
            confirmText = stringResource(R.string.action_delete),
            dismissText = stringResource(R.string.action_cancel)
        )
    }
    val navController = LocalNavController.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val sb = remember { scrollBehavior }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.storyStringCompileState) {
        when (state.storyStringCompileState) {
            is ContextTemplateScreenState.StoryStringCompileState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        state.storyStringCompileState.message,
                        withDismissAction = true
                    )
                }
            }

            else -> snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
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
                        menuItems = dropdownMenuItems(onEvent),
                        iconVector = Icons.Default.MoreVert
                    )
                },
                scrollBehavior = sb
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        ContextTemplateScreenBody(
            templates = state.templates,
            storyStringCompileState = state.storyStringCompileState,
            currentTemplate = state.currentTemplate,
            onEvent = onEvent,
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(sb.nestedScrollConnection)
        )
    }
}

@Composable
fun ContextTemplateScreenBody(
    templates: ImmutableList<DisplayContextTemplate>,
    currentTemplate: DisplayContextTemplate,
    storyStringCompileState: ContextTemplateScreenState.StoryStringCompileState,
    onEvent: OnEvent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val horizontalPadding = PaddingValues(horizontal = 8.dp)
        DropdownTextField(
            value = remember(currentTemplate.name) { ComposeString.Raw(currentTemplate.name) },
            variants = remember(templates) {
                templates.map {
                    SimpleDropDownMenuItem(
                        string = ComposeString.Raw(it.name),
                        onClick = { onEvent(UserEvent.SelectTemplate(it.id)) }
                    )
                }.toImmutableList()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontalPadding),
            maxLines = 2
        )

        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontalPadding),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            CardBody(
                storyString = currentTemplate.storyString,
                storyStringCompileState = storyStringCompileState,
                chatStart = currentTemplate.chatStart,
                exampleSeparator = currentTemplate.exampleSeparator,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

private const val MAX_TITLE_LINES = 3

@Composable
fun CardBody(
    storyString: String,
    storyStringCompileState: ContextTemplateScreenState.StoryStringCompileState,
    chatStart: String,
    exampleSeparator: String,
    onEvent: OnEvent,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp),
) {
    val inner = Modifier.fillMaxWidth()
    Title(
        text = stringResource(R.string.label_story_string_setting),
        tooltip = stringResource(R.string.tooltip_story_string_setting),
        modifier = inner
    )
    OutlinedTextField(
        value = storyString,
        onValueChange = { onEvent(UserEvent.UpdateStoryString(it)) },
        modifier = inner.clearFocusOnSoftKeyboardHide(),
        isError = storyStringCompileState is ContextTemplateScreenState.StoryStringCompileState.Error
    )

    Title(
        text = stringResource(R.string.label_example_separator_setting),
        tooltip = stringResource(R.string.tooltip_example_separator_setting),
        modifier = inner
    )
    OutlinedTextField(
        value = exampleSeparator, onValueChange = { onEvent(UserEvent.UpdateExampleSeparator(it)) },
        modifier = inner.clearFocusOnSoftKeyboardHide()
    )

    Title(
        text = stringResource(R.string.label_chat_start_setting),
        tooltip = stringResource(R.string.tooltip_chat_start_setting),
        modifier = inner
    )
    OutlinedTextField(
        value = chatStart, onValueChange = { onEvent(UserEvent.UpdateChatStart(it)) },
        modifier = inner.clearFocusOnSoftKeyboardHide()
    )
}

@Composable
private fun Title(
    text: String,
    tooltip: String,
    modifier: Modifier = Modifier
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Text(
        text = text,
        modifier = Modifier.weight(1f),
        maxLines = MAX_TITLE_LINES,
        style = MaterialTheme.typography.titleLarge
    )
    TooltipIconButton(
        icon = Icons.Default.QuestionMark,
        text = tooltip
    )
}

@Composable
private fun dropdownMenuItems(onEvent: OnEvent) = remember(onEvent) {
    listOf(
        SimpleDropDownMenuItem(
            string = ComposeString.Resource(R.string.menu_item_rename_template),
            onClick = { onEvent(UserEvent.RenameTemplate) },
            iconVector = Icons.Default.Edit
        ),
        SimpleDropDownMenuItem(
            string = ComposeString.Resource(R.string.menu_item_update_current_template),
            onClick = { onEvent(UserEvent.SaveCurrentTemplate) },
            iconVector = Icons.Default.Save
        ),
        SimpleDropDownMenuItem(
            string = ComposeString.Resource(R.string.manu_item_save_template_as),
            onClick = { onEvent(UserEvent.SaveAs) },
            iconVector = Icons.Default.SaveAs
        ),
        SimpleDropDownMenuItem(
            string = ComposeString.Resource(R.string.menu_item_delete_template),
            onClick = { onEvent(UserEvent.DeleteTemplate) },
            iconVector = Icons.Default.DeleteForever
        )
    )
}


@Preview
@Composable
private fun ContextTemplateScreenPreview() = ICPreview {
    ContextTemplateScreen(
        state = ContextTemplateScreenState(
            currentTemplate = ContextTemplate.default(name = "Current Template").toDisplay()
        ),
        onEvent = {},
        modifier = Modifier.fillMaxSize()
    )
}