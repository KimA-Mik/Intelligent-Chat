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
    private val currentTemplatesUseCase =
        MutableStateFlow(InstructModeTemplate.default().toDisplay())
    private val includeNamePolicyDialog = MutableStateFlow(false)
    val state = combine(
        currentTemplatesUseCase,
        subscribeToInstructModeTemplates().map { list ->
            list.map { it.toListItem() }.toImmutableList()
        },
        includeNamePolicyDialog
    ) { currentTemplate, templates, includeNamePolicyDialog ->
        InstructModeTemplateScreenState(
            currentTemplate = currentTemplate,
            templates = templates,
            includeNamePolicyDialog = includeNamePolicyDialog
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
        if (policy == currentTemplatesUseCase.value.includeNamePolicy) return

        currentTemplatesUseCase.update {
            it.copy(includeNamePolicy = policy)
        }
    }

    private fun getCurrentTemplate() {
        viewModelScope.launch {
            currentTemplatesUseCase.value = getSelectedInstructTemplate().toDisplay()
        }
    }
}