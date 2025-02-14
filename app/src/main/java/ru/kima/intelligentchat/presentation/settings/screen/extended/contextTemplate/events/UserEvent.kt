package ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.events

sealed interface UserEvent {
    data class SelectTemplate(val id: Long) : UserEvent
    data class UpdateStoryString(val value: String) : UserEvent
    data class UpdateExampleSeparator(val value: String) : UserEvent
    data class UpdateChatStart(val value: String) : UserEvent
    data class UpdateDialogBuffer(val value: String) : UserEvent
    data object SaveCurrentTemplate : UserEvent
    data object RenameTemplate : UserEvent
    data object AcceptRenameTemplateDialog : UserEvent
    data object DismissRenameTemplateDialog : UserEvent
    data object SaveAs : UserEvent
    data object AcceptSaveAsDialog : UserEvent
    data object DismissSaveAsDialog : UserEvent
    data object DeleteTemplate : UserEvent
    data object AcceptDeleteTemplateDialog : UserEvent
    data object DismissDeleteTemplateDialog : UserEvent
}