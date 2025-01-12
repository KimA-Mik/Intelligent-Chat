package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.messaging.instructMode.model.IncludeNamePolicy
import ru.kima.intelligentchat.domain.messaging.instructMode.model.InstructModeTemplate
import ru.kima.intelligentchat.domain.messaging.instructMode.useCase.GetSelectedInstructTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.instructMode.useCase.SubscribeToInstructModeTemplatesUseCase
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.events.UserEvent
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model.toDisplay
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model.toListItem

class InstructModeTemplateViewModel(
    private val subscribeToInstructModeTemplates: SubscribeToInstructModeTemplatesUseCase,
    private val getSelectedInstructTemplate: GetSelectedInstructTemplateUseCase,
) : ViewModel() {
    private val currentTemplate =
        MutableStateFlow(InstructModeTemplate.default().toDisplay())
    private val includeNamePolicyDialog = MutableStateFlow(false)
    private val renameTemplateDialog = MutableStateFlow(false)
    private val renameTemplateDialogValue = MutableStateFlow("")
    val state = combine(
        currentTemplate,
        subscribeToInstructModeTemplates().map { list ->
            list.map { it.toListItem() }.toImmutableList()
        },
        includeNamePolicyDialog,
        renameTemplateDialog,
        renameTemplateDialogValue
    ) { currentTemplate, templates, includeNamePolicyDialog, renameTemplateDialog, renameTemplateDialogValue ->
        InstructModeTemplateScreenState(
            currentTemplate = currentTemplate,
            templates = templates,
            includeNamePolicyDialog = includeNamePolicyDialog,
            renameTemplateDialog = renameTemplateDialog,
            renameTemplateDialogValue = renameTemplateDialogValue
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        InstructModeTemplateScreenState()
    )

    init {
        getCurrentTemplate()
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

    private fun getCurrentTemplate() {
        viewModelScope.launch {
            currentTemplate.value = getSelectedInstructTemplate().toDisplay()
        }
    }
}
