package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model

import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate

data class DisplayInstructModeTemplateListItem(val id: Long, val name: String)

fun InstructModeTemplate.toListItem() = DisplayInstructModeTemplateListItem(
    id = id,
    name = name
)