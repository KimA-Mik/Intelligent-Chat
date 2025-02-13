package ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase.GetSelectedContextTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase.SubscribeToContextTemplatesUseCase
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.model.DisplayContextTemplate
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.model.toDisplay

class ContextTemplateViewModel(
    subscribeToContextTemplates: SubscribeToContextTemplatesUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val currentContextTemplate: GetSelectedContextTemplateUseCase,
) : ViewModel() {
    private val renameDialog = MutableStateFlow(false)
    private val saveAsDialog = MutableStateFlow(false)
    private val dialogBuffer = MutableStateFlow("")

    init {
        val currentTemplateId = savedStateHandle.get<Long>(CURRENT_TEMPLATE_ID)
        if (currentTemplateId == null || currentTemplateId == 0L) {
            viewModelScope.launch {
                getCurrentTemplate()
            }
        }
    }

    private val currentTemplate = combine(
        savedStateHandle.getStateFlow(CURRENT_TEMPLATE_ID, 0L),
        savedStateHandle.getStateFlow(CURRENT_TEMPLATE_NAME, ""),
        savedStateHandle.getStateFlow(CURRENT_TEMPLATE_STORY_STRING, ""),
        savedStateHandle.getStateFlow(CURRENT_TEMPLATE_EXAMPLE_SEPARATOR, ""),
        savedStateHandle.getStateFlow(CURRENT_TEMPLATE_CHAT_START, "")
    ) { id, name, storyString, exampleSeparator, chatStart ->
        DisplayContextTemplate(
            id = id,
            name = name,
            storyString = storyString,
            exampleSeparator = exampleSeparator,
            chatStart = chatStart
        )
    }

    val state = combine(
        subscribeToContextTemplates().map { it.map(ContextTemplate::toDisplay).toImmutableList() },
        currentTemplate, renameDialog, saveAsDialog, dialogBuffer
    ) { templates, currentTemplate, renameDialog, saveAsDialog, dialogBuffer ->
        ContextTemplateScreenState(
            templates = templates,
            currentTemplate = currentTemplate,
            renameDialog = renameDialog,
            saveAsDialog = saveAsDialog,
            dialogBuffer = dialogBuffer
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ContextTemplateScreenState())

    private suspend fun getCurrentTemplate() {
        val currentContextTemplate = currentContextTemplate()
        savedStateHandle[CURRENT_TEMPLATE_ID] = currentContextTemplate.id
        savedStateHandle[CURRENT_TEMPLATE_NAME] = currentContextTemplate.name
        savedStateHandle[CURRENT_TEMPLATE_STORY_STRING] = currentContextTemplate.storyString
        savedStateHandle[CURRENT_TEMPLATE_EXAMPLE_SEPARATOR] =
            currentContextTemplate.exampleSeparator
        savedStateHandle[CURRENT_TEMPLATE_CHAT_START] = currentContextTemplate.chatStart
    }

    companion object {
        private const val CURRENT_TEMPLATE_ID = "id"
        private const val CURRENT_TEMPLATE_NAME = "name"
        private const val CURRENT_TEMPLATE_STORY_STRING = "story_string"
        private const val CURRENT_TEMPLATE_EXAMPLE_SEPARATOR = "example_separator"
        private const val CURRENT_TEMPLATE_CHAT_START = "chat_start"
    }
}