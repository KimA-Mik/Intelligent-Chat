package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model.DisplayInstructModeTemplate
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model.DisplayInstructModeTemplateListItem
import ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model.toDisplay

data class InstructModeTemplateScreenState(
    val currentTemplate: DisplayInstructModeTemplate = InstructModeTemplate.default().toDisplay(),
    val templates: ImmutableList<DisplayInstructModeTemplateListItem> = persistentListOf(),
    val dialogs: Dialogs = Dialogs(),
    val sections: Sections = Sections()
) {
    data class Dialogs(
        val includeNamePolicyDialog: Boolean = false,
        val renameTemplateDialog: Boolean = false,
        val renameTemplateDialogValue: String = "",
        val deleteTemplateDialog: Boolean = false
    )

    data class Sections(
        val userStrings: Boolean = true,
        val assistantStrings: Boolean = false,
        val anotherStringsSection: Boolean = false
    )
}
