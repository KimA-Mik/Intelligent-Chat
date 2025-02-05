package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.events

import ru.kima.intelligentchat.domain.messaging.instructMode.model.IncludeNamePolicy

sealed interface UserEvent {
    data class SelectTemplate(val id: Long) : UserEvent
    data object OpenSelectIncludeNamePolicy : UserEvent
    data object DismissSelectIncludeNamePolicyDialog : UserEvent
    data class SelectIncludeNamePolicy(val policy: IncludeNamePolicy) : UserEvent
    data class CreateTemplate(val name: String) : UserEvent
    data object OpenRenameTemplateDialog : UserEvent
    data object AcceptRenameTemplateDialog : UserEvent
    data object DismissRenameTemplateDialog : UserEvent
    data class UpdateRenameTemplateDialog(val value: String) : UserEvent
    data class UpdateWrapSequencesWithNewLine(val value: Boolean) : UserEvent
    data class SwitchUserStringsSection(val value: Boolean) : UserEvent
    data class UpdateUserPrefix(val value: String) : UserEvent
    data class UpdateUserPostfix(val value: String) : UserEvent
    data class SwitchAssistantStringsSection(val value: Boolean) : UserEvent
    data class UpdateAssistantPrefix(val value: String) : UserEvent
    data class UpdateAssistantPostfix(val value: String) : UserEvent
    data class SwitchAnotherStringsSection(val value: Boolean) : UserEvent
    data class UpdateFirstUserPrefix(val value: String) : UserEvent
    data class UpdateLastUserPrefix(val value: String) : UserEvent
    data class UpdateFirstAssistantPrefix(val value: String) : UserEvent
    data class UpdateLastAssistantPrefix(val value: String) : UserEvent
}