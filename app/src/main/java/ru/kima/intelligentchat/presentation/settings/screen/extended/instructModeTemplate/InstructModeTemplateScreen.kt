package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.domain.messaging.instructMode.model.IncludeNamePolicy
import ru.kima.intelligentchat.presentation.common.components.AppBar
import ru.kima.intelligentchat.presentation.settings.screen.components.widgets.SwitchSettingWidget
import ru.kima.intelligentchat.presentation.settings.screen.components.widgets.TextSettingWidget
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.dialogs.IncludeNamePolicySelectDialog
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.dialogs.RenameTemplateDialog
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.events.UserEvent
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model.DisplayInstructModeTemplate
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model.DisplayInstructModeTemplateListItem
import ru.kima.intelligentchat.presentation.settings.util.composeSting
import ru.kima.intelligentchat.presentation.ui.LocalNavController
import ru.kima.intelligentchat.presentation.ui.components.DropdownTextField
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.util.preview.ICPreview

const val MAX_TEMPLATE_TITLE_LINES = 2

@Composable
fun InstructModeTemplateRoot() {
    val viewModel: InstructModeTemplateViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = remember<(UserEvent) -> Unit> {
        {
            viewModel.onEvent(it)
        }
    }

    InstructModeTemplateScreen(
        state = state,
        onEvent = onEvent,
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructModeTemplateScreen(
    state: InstructModeTemplateScreenState,
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.includeNamePolicyDialog ->
            IncludeNamePolicySelectDialog(
                selectedPolicy = state.currentTemplate.includeNamePolicy,
                onAccept = { onEvent(UserEvent.SelectIncludeNamePolicy(it)) },
                onDismiss = { onEvent(UserEvent.DismissSelectIncludeNamePolicyDialog) },
            )

        state.renameTemplateDialog ->
            RenameTemplateDialog(
                value = state.renameTemplateDialogValue,
                onAccept = { onEvent(UserEvent.AcceptRenameTemplateDialog) },
                onDismiss = { onEvent(UserEvent.DismissRenameTemplateDialog) },
                onValueChange = { onEvent(UserEvent.UpdateRenameTemplateDialog(it)) },
            )
    }

    val nacController = LocalNavController.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                titleContent = { Text(stringResource(R.string.instruct_mode_setting_title)) },
                navigateUp = { nacController.popBackStack() },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        InstructModeTemplateContent(
            state = state,
            onEvent = onEvent,
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}

@Composable
fun InstructModeTemplateContent(
    state: InstructModeTemplateScreenState,
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val horizontalPadding = 8.dp
        SelectTemplateCard(
            currentTemplateTitle = state.currentTemplate.name,
            templates = state.templates,
            onEvent = onEvent,
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )
        SelectedIncludeNamePolicy(
            includeNamePolicy = state.currentTemplate.includeNamePolicy,
            onClick = { onEvent(UserEvent.OpenSelectIncludeNamePolicy) },
            modifier = Modifier.fillMaxWidth()
        )
        SwitchSettingWidget(
            title = remember { ComposeString.Resource(R.string.wrap_sequences_with_new_line_setting_title) },
            checked = state.currentTemplate.wrapSequencesWithNewLine,
            onCheckedChange = { onEvent(UserEvent.UpdateWrapSequencesWithNewLine(it)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SelectedIncludeNamePolicy(
    includeNamePolicy: IncludeNamePolicy,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextSettingWidget(
        title = ComposeString.Resource(R.string.include_string_policy_setting_title),
        modifier = modifier.clickable(onClick = onClick),
        subtitle = includeNamePolicy.composeSting()
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SelectTemplateCard(
    currentTemplateTitle: String,
    templates: ImmutableList<DisplayInstructModeTemplateListItem>,
    onEvent: (UserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectTextBox(
                currentTemplateTitle = currentTemplateTitle,
                templates = templates,
                selectTemplate = { onEvent(UserEvent.SelectTemplate(it)) },
                modifier = Modifier.weight(1f),
            )
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text(text = stringResource(R.string.tooltip_instruct_mode_template_rename))
                    }
                },
                state = rememberTooltipState()
            ) {
                IconButton(onClick = { onEvent(UserEvent.OpenRenameTemplateDialog) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                }
            }

            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Text("Tooltip")
                    }
                },
                state = rememberTooltipState()
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun SelectTextBox(
    currentTemplateTitle: String,
    templates: ImmutableList<DisplayInstructModeTemplateListItem>,
    selectTemplate: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownTextField(
        value = remember(currentTemplateTitle) { ComposeString.Raw(currentTemplateTitle) },
        variants = remember(templates) {
            templates.fastMap {
                SimpleDropDownMenuItem(
                    string = ComposeString.Raw(it.name),
                    onClick = { selectTemplate(it.id) },
                )
            }.toImmutableList()
        },
        modifier = modifier,
        maxLines = MAX_TEMPLATE_TITLE_LINES
    )
}


@Preview
@Composable
private fun InstructModeTemplateScreenPreview() = ICPreview {
    InstructModeTemplateScreen(InstructModeTemplateScreenState(
        currentTemplate = DisplayInstructModeTemplate(
            id = 1,
            name = "Template",
            includeNamePolicy = IncludeNamePolicy.ALWAYS,
            wrapSequencesWithNewLine = true,
            userMessagePrefix = "",
            userMessageSuffix = "",
            assistantMessagePrefix = "",
            assistantMessageSuffix = "",
            systemSameAsUser = false,
            firstAssistantPrefix = "",
            lastAssistantPrefix = "",
            firstUserPrefix = "",
            lastUserPrefix = ""
        )
    ), onEvent = {})
}

