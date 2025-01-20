package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.messaging.instructMode.model.IncludeNamePolicy
import ru.kima.intelligentchat.domain.messaging.instructMode.model.InstructModeTemplate
import ru.kima.intelligentchat.domain.messaging.instructMode.useCase.GetSelectedInstructTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.instructMode.useCase.SubscribeToInstructModeTemplatesUseCase
import ru.kima.intelligentchat.domain.messaging.instructMode.useCase.UpdateInstructModeTemplateUseCase
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.events.UserEvent
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model.toDisplay
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model.toListItem
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model.toModel

class InstructModeTemplateViewModel(
    private val subscribeToInstructModeTemplates: SubscribeToInstructModeTemplatesUseCase,
    private val getSelectedInstructTemplate: GetSelectedInstructTemplateUseCase,
    private val updateInstructModeTemplate: UpdateInstructModeTemplateUseCase
) : ViewModel() {
    private val currentTemplate =
        MutableStateFlow(InstructModeTemplate.default().toDisplay())
    private val includeNamePolicyDialog = MutableStateFlow(false)
    private val renameTemplateDialog = MutableStateFlow(false)
    private val renameTemplateDialogValue = MutableStateFlow("")

    private val dialogs = combine(
        includeNamePolicyDialog,
        renameTemplateDialog,
        renameTemplateDialogValue
    ) { includeNamePolicyDialog,
        renameTemplateDialog,
        renameTemplateDialogValue ->
        InstructModeTemplateScreenState.Dialogs(
            includeNamePolicyDialog = includeNamePolicyDialog,
            renameTemplateDialog = renameTemplateDialog,
            renameTemplateDialogValue = renameTemplateDialogValue
        )
    }

    private val userStringsSection = MutableStateFlow(true)
    private val assistantStringSection = MutableStateFlow(false)
    private val anotherStringsSection = MutableStateFlow(false)

    private val sections = combine(
        userStringsSection,
        assistantStringSection,
        anotherStringsSection
    ) { userStrings, assistantStrings, anotherStringsSection ->
        InstructModeTemplateScreenState.Sections(
            userStrings = userStrings,
            assistantStrings = assistantStrings,
            anotherStringsSection = anotherStringsSection
        )
    }

    val state = combine(
        currentTemplate,
        subscribeToInstructModeTemplates().map { list ->
            list.map { it.toListItem() }.toImmutableList()
        },
        dialogs, sections
    ) { currentTemplate, templates, dialogs, sections ->
        InstructModeTemplateScreenState(
            currentTemplate = currentTemplate,
            templates = templates,
            dialogs = dialogs,
            sections = sections
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        InstructModeTemplateScreenState()
    )

    private var updateJob: Job? = null

    init {
        getCurrentTemplate()
    }

    override fun onCleared() {
        viewModelScope.launch {
            updateInstructModeTemplate(currentTemplate.value.toModel())
        }
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.SelectTemplate -> onSelectTemplate(event.id)
            UserEvent.OpenSelectIncludeNamePolicy -> onOpenSelectIncludeNamePolicy()
            UserEvent.DismissSelectIncludeNamePolicyDialog -> onDismissSelectIncludeNamePolicyDialog()
            is UserEvent.SelectIncludeNamePolicy -> onSelectIncludeNamePolicy(event.policy)
            UserEvent.OpenRenameTemplateDialog -> onOpenRenameTemplateDialog()
            UserEvent.AcceptRenameTemplateDialog -> onAcceptRenameTemplateDialog()
            UserEvent.DismissRenameTemplateDialog -> onDismissRenameTemplateDialog()
            is UserEvent.UpdateRenameTemplateDialog -> onUpdateRenameTemplateDialog(event.value)
            is UserEvent.UpdateWrapSequencesWithNewLine -> onUpdateWrapSequencesWithNewLine(event.value)
            is UserEvent.SwitchUserStringsSection -> onSwitchUserStringsSection(event.value)
            is UserEvent.UpdateUserPrefix -> onUpdateUserPrefix(event.value)
            is UserEvent.UpdateUserPostfix -> onUpdateUserPostfix(event.value)
            is UserEvent.SwitchAssistantStringsSection -> onSwitchAssistantStringsSection(event.value)
            is UserEvent.UpdateAssistantPrefix -> onUpdateAssistantPrefix(event.value)
            is UserEvent.UpdateAssistantPostfix -> onUpdateAssistantPostfix(event.value)
            is UserEvent.SwitchAnotherStringsSection -> onSwitchAnotherStringsSection(event.value)
            is UserEvent.UpdateFirstUserPrefix -> onUpdateFirstUserPrefix(event.value)
            is UserEvent.UpdateLastUserPrefix -> onUpdateLastUserPrefix(event.value)
            is UserEvent.UpdateFirstAssistantPrefix -> onUpdateFirstAssistantPrefix(event.value)
            is UserEvent.UpdateLastAssistantPrefix -> onUpdateLastAssistantPrefix(event.value)
        }
    }

    private fun onSelectTemplate(id: Long) {

    }

    private fun onOpenSelectIncludeNamePolicy() {
        includeNamePolicyDialog.value = true
    }

    private fun onDismissSelectIncludeNamePolicyDialog() {
        includeNamePolicyDialog.value = false
    }

    private fun onSelectIncludeNamePolicy(policy: IncludeNamePolicy) {
        includeNamePolicyDialog.value = false
        if (policy == currentTemplate.value.includeNamePolicy) return

        currentTemplate.update {
            it.copy(includeNamePolicy = policy)
        }
    }

    private fun onOpenRenameTemplateDialog() {
        renameTemplateDialog.value = true
        renameTemplateDialogValue.value = currentTemplate.value.name
    }

    private fun onAcceptRenameTemplateDialog() {
        renameTemplateDialog.value = false
        if (renameTemplateDialogValue.value == currentTemplate.value.name) return

        currentTemplate.update {
            it.copy(name = renameTemplateDialogValue.value)
        }
    }

    private fun onDismissRenameTemplateDialog() {
        renameTemplateDialog.value = false
    }

    private fun onUpdateRenameTemplateDialog(value: String) {
        renameTemplateDialogValue.value = value
    }

    private fun onUpdateWrapSequencesWithNewLine(value: Boolean) {
        currentTemplate.update {
            it.copy(
                wrapSequencesWithNewLine = value
            )
        }
    }

    private fun onSwitchUserStringsSection(value: Boolean) {
        userStringsSection.value = value
    }

    private fun onUpdateUserPrefix(value: String) {
        currentTemplate.update {
            it.copy(userMessagePrefix = value)
        }
    }

    private fun onUpdateUserPostfix(value: String) {
        currentTemplate.update {
            it.copy(userMessagePostfix = value)
        }
    }

    private fun onSwitchAssistantStringsSection(value: Boolean) {
        assistantStringSection.value = value
    }

    private fun onUpdateAssistantPrefix(value: String) {
        currentTemplate.update {
            it.copy(
                assistantMessagePrefix = value
            )
        }
    }

    private fun onUpdateAssistantPostfix(value: String) {
        currentTemplate.update {
            it.copy(
                assistantMessagePostfix = value
            )
        }
    }

    private fun onSwitchAnotherStringsSection(value: Boolean) {
        anotherStringsSection.value = value
    }

    private fun onUpdateFirstUserPrefix(value: String) {
        currentTemplate.update {
            it.copy(
                firstUserPrefix = value
            )
        }
    }

    private fun onUpdateLastUserPrefix(value: String) {
        currentTemplate.update {
            it.copy(
                lastUserPrefix = value
            )
        }
    }

    private fun onUpdateFirstAssistantPrefix(value: String) {
        currentTemplate.update {
            it.copy(
                firstAssistantPrefix = value
            )
        }
    }

    private fun onUpdateLastAssistantPrefix(value: String) {
        currentTemplate.update {
            it.copy(
                lastAssistantPrefix = value
            )
        }
    }

    @OptIn(FlowPreview::class)
    private fun getCurrentTemplate() {
        updateJob?.cancel()
        viewModelScope.launch {
            currentTemplate.value = getSelectedInstructTemplate().toDisplay()
            updateJob = viewModelScope.launch {
                currentTemplate.debounce(SAVE_TIMEOUT).collect {
                    updateInstructModeTemplate(it.toModel())
                }
            }
        }
    }

    companion object {
        private const val SAVE_TIMEOUT = 500L
    }
}
