package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.events

import ru.kima.intelligentchat.domain.messaging.instructMode.model.IncludeNamePolicy

sealed interface UserEvent {
    data class SelectTemplate(val id: Long) : UserEvent
    data object OpenSelectIncludeNamePolicy : UserEvent
    data object DismissSelectIncludeNamePolicyDialog : UserEvent
    data class SelectIncludeNamePolicy(val policy: IncludeNamePolicy) : UserEvent
    data object OpenRenameTemplateDialog : UserEvent
    data object AcceptRenameTemplateDialog : UserEvent
    data object DismissRenameTemplateDialog : UserEvent
    data class UpdateRenameTemplateDialog(val value: String) : UserEvent
    data class UpdateWrapSequencesWithNewLine(val value: Boolean) : UserEvent
}