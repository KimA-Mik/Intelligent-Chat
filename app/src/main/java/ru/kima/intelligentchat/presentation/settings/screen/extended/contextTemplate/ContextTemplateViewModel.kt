package ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase.DeleteContextTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase.GetSelectedContextTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase.InsertContextTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase.SelectContextTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase.SubscribeToContextTemplatesUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase.UpdateContextTemplateUseCase
import ru.kima.intelligentchat.domain.preferences.advancedFormatting.useCase.ValidateTemplateUseCase
import ru.kima.intelligentchat.domain.utils.combine
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.events.UserEvent
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.model.DisplayContextTemplate
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.model.toDisplay
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.model.toModel

@OptIn(FlowPreview::class)
class ContextTemplateViewModel(
    subscribeToContextTemplates: SubscribeToContextTemplatesUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val deleteContextTemplate: DeleteContextTemplateUseCase,
    private val currentContextTemplate: GetSelectedContextTemplateUseCase,
    private val insertContextTemplate: InsertContextTemplateUseCase,
    private val selectContextTemplate: SelectContextTemplateUseCase,
    private val updateContextTemplate: UpdateContextTemplateUseCase,
    private val validateTemplate: ValidateTemplateUseCase
) : ViewModel() {
    private val renameDialog = MutableStateFlow(false)
    private val saveAsDialog = MutableStateFlow(false)
    private val dialogBuffer = MutableStateFlow("")
    private val deleteDialog = MutableStateFlow(false)
    private val storyStringCompileState =
        MutableStateFlow<ContextTemplateScreenState.StoryStringCompileState>(
            ContextTemplateScreenState.StoryStringCompileState.Ok
        )

    private val storyString = savedStateHandle.getStateFlow(CURRENT_TEMPLATE_STORY_STRING, "")

    init {
        val currentTemplateId = savedStateHandle.get<Long>(CURRENT_TEMPLATE_ID)
        if (currentTemplateId == null || currentTemplateId == 0L) {
            viewModelScope.launch {
                getCurrentTemplate()
            }

            storyString.debounce(500L).onEach {
                storyStringCompileState.value = when (val res = validateTemplate(it)) {
                    is ValidateTemplateUseCase.Result.Error ->
                        ContextTemplateScreenState.StoryStringCompileState.Error(res.message)

                    ValidateTemplateUseCase.Result.Success -> ContextTemplateScreenState.StoryStringCompileState.Ok
                }
            }.flowOn(Dispatchers.Default).launchIn(viewModelScope)
        }
    }

    private val currentTemplate = combine(
        savedStateHandle.getStateFlow(CURRENT_TEMPLATE_ID, 0L),
        savedStateHandle.getStateFlow(CURRENT_TEMPLATE_NAME, ""),
        storyString,
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
        currentTemplate,
        renameDialog,
        saveAsDialog,
        dialogBuffer,
        deleteDialog,
        storyStringCompileState
    ) { templates, currentTemplate, renameDialog, saveAsDialog, dialogBuffer, deleteDialog, storyStringCompileState ->
        ContextTemplateScreenState(
            templates = templates,
            currentTemplate = currentTemplate,
            renameDialog = renameDialog,
            saveAsDialog = saveAsDialog,
            dialogBuffer = dialogBuffer,
            deleteDialog = deleteDialog,
            storyStringCompileState = storyStringCompileState
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ContextTemplateScreenState())

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.SelectTemplate -> onSelectTemplate(event.id)
            is UserEvent.UpdateStoryString -> onUpdateStoryString(event.value)
            is UserEvent.UpdateExampleSeparator -> onUpdateExampleSeparator(event.value)
            is UserEvent.UpdateChatStart -> onUpdateChatStart(event.value)
            is UserEvent.UpdateDialogBuffer -> onUpdateDialogBuffer(event.value)
            UserEvent.SaveCurrentTemplate -> onSaveCurrentTemplate()
            UserEvent.RenameTemplate -> onRenameTemplate()
            UserEvent.AcceptRenameTemplateDialog -> onAcceptRenameTemplateDialog()
            UserEvent.DismissRenameTemplateDialog -> onDismissRenameTemplateDialog()
            UserEvent.SaveAs -> onSaveAs()
            UserEvent.AcceptSaveAsDialog -> onAcceptSaveAsDialog()
            UserEvent.DismissSaveAsDialog -> onDismissSaveAsDialog()
            UserEvent.DeleteTemplate -> onDeleteTemplate()
            UserEvent.AcceptDeleteTemplateDialog -> onAcceptDeleteTemplateDialog()
            UserEvent.DismissDeleteTemplateDialog -> onDismissDeleteTemplateDialog()
        }
    }

    private fun onDeleteTemplate() {
        deleteDialog.value = true
    }

    private fun onAcceptDeleteTemplateDialog() {
        deleteDialog.value = false
        viewModelScope.launch {
            deleteContextTemplate(state.value.currentTemplate.id)
            getCurrentTemplate()
        }
    }

    private fun onDismissDeleteTemplateDialog() {
        deleteDialog.value = false
    }

    private fun onDismissSaveAsDialog() {
        saveAsDialog.value = false
    }

    private fun onAcceptSaveAsDialog() {
        saveAsDialog.value = false
        viewModelScope.launch {
            val newId = insertContextTemplate(
                state.value.currentTemplate.copy(
                    id = 0L,
                    name = dialogBuffer.value
                ).toModel()
            )
            selectContextTemplate(newId)
            getCurrentTemplate()
        }
    }

    private fun onSaveAs() {
        dialogBuffer.value = state.value.currentTemplate.name
        saveAsDialog.value = true
    }

    private fun onDismissRenameTemplateDialog() {
        renameDialog.value = false
    }

    private fun onAcceptRenameTemplateDialog() {
        renameDialog.value = false
        val template =
            state.value.templates.find { it.id == state.value.currentTemplate.id } ?: return
        viewModelScope.launch {
            updateContextTemplate(template.copy(name = dialogBuffer.value).toModel())
            savedStateHandle[CURRENT_TEMPLATE_NAME] = dialogBuffer.value
        }
    }

    private fun onRenameTemplate() {
        dialogBuffer.value = state.value.currentTemplate.name
        renameDialog.value = true
    }

    private fun onSaveCurrentTemplate() = viewModelScope.launch {
        updateContextTemplate(state.value.currentTemplate.toModel())
    }

    private fun onUpdateDialogBuffer(value: String) {
        dialogBuffer.value = value
    }

    private fun onUpdateStoryString(value: String) {
        savedStateHandle[CURRENT_TEMPLATE_STORY_STRING] = value
    }

    private fun onUpdateExampleSeparator(value: String) {
        savedStateHandle[CURRENT_TEMPLATE_EXAMPLE_SEPARATOR] = value
    }

    private fun onUpdateChatStart(value: String) {
        savedStateHandle[CURRENT_TEMPLATE_CHAT_START] = value
    }

    private fun onSelectTemplate(id: Long) = viewModelScope.launch {
        selectContextTemplate(id)
        getCurrentTemplate()
    }

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