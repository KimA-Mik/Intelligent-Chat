package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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

    val state = combine(
        currentTemplatesUseCase,
        subscribeToInstructModeTemplates().map { list ->
            list.map { it.toListItem() }.toImmutableList()
        }
    ) { currentTemplate, templates ->
        InstructModeTemplateScreenState(
            currentTemplate = currentTemplate,
            templates = templates
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

    }

    private fun getCurrentTemplate() {
        viewModelScope.launch {
            currentTemplatesUseCase.value = getSelectedInstructTemplate().toDisplay()
        }
    }
}