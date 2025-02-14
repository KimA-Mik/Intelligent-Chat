package ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.events

sealed interface UserEvent {
    data class UpdateStoryString(val value: String) : UserEvent
    data class UpdateExampleSeparator(val value: String) : UserEvent
    data class UpdateChatStart(val value: String) : UserEvent
    data class UpdateDialogBuffer(val value: String) : UserEvent
    data object RenameTemplate : UserEvent
    data object AcceptRenameTemplateDialog : UserEvent
    data object DismissRenameTemplateDialog : UserEvent
}