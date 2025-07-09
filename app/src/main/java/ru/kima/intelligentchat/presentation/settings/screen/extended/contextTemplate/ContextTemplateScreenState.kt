package ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.model.DisplayContextTemplate
import ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.model.toDisplay

@Immutable
data class ContextTemplateScreenState(
    val templates: ImmutableList<DisplayContextTemplate> = persistentListOf(),
    val currentTemplate: DisplayContextTemplate = ContextTemplate.default().toDisplay(),
    val renameDialog: Boolean = false,
    val saveAsDialog: Boolean = false,
    val dialogBuffer: String = "",
    val deleteDialog: Boolean = false,
    val storyStringCompileState: StoryStringCompileState = StoryStringCompileState.OK
) {
    enum class StoryStringCompileState {
        OK, ERROR
    }
}
