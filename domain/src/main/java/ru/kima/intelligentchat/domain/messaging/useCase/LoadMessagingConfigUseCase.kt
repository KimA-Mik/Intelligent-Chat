package ru.kima.intelligentchat.domain.messaging.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.common.ApiType
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase.GetSelectedContextTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase.GetSelectedInstructTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.model.MessagingConfig
import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class LoadMessagingConfigUseCase(
    private val hordeStateRepository: HordeStateRepository,
    private val getSelectedContextTemplate: GetSelectedContextTemplateUseCase,
    private val getSelectedInstructTemplate: GetSelectedInstructTemplateUseCase
) {
    suspend operator fun invoke(
        apiType: ApiType,
        senderType: SenderType,
        personaName: String,
        cardName: String
    ) = when (apiType) {
        ApiType.KOBOLD_AI -> loadHordeConfig(senderType, personaName, cardName)
        ApiType.HORDE -> loadHordeConfig(senderType, personaName, cardName)
    }


    //TODO: Properly handle stop-sequences
    private suspend fun loadHordeConfig(
        senderType: SenderType,
        personaName: String,
        cardName: String
    ): MessagingConfig {
        val state = hordeStateRepository.hordeState().first()
        val instructModeTemplate = getSelectedInstructTemplate()
        return MessagingConfig(
            maxResponseLength = state.actualResponseLength,
            maxContextLength = state.actualContextSize,
            stopSequence = getStopSequence(senderType, personaName, cardName, instructModeTemplate),
            contextTemplate = getSelectedContextTemplate(),
            instructModeTemplate = instructModeTemplate
        )
    }

    private fun getStopSequence(
        senderType: SenderType,
        personaName: String,
        cardName: String,
        instructModeTemplate: InstructModeTemplate,
    ): List<String> = buildList {
        when (senderType) {
            SenderType.Character -> {
                add("\n$personaName:")
            }

            SenderType.Persona -> {
                add("\n$cardName:")
            }
        }

        //TODO: Check necessity of leading '\n'
        if (instructModeTemplate.lastUserPrefix.isNotBlank()) {
            add(instructModeTemplate.lastUserPrefix)
        }
        if (instructModeTemplate.userMessagePrefix.isNotBlank()) {
            add(instructModeTemplate.userMessagePrefix)
        }
        if (instructModeTemplate.lastAssistantPrefix.isNotBlank()) {
            add(instructModeTemplate.lastAssistantPrefix)
        }
        if (instructModeTemplate.assistantMessagePrefix.isNotBlank()) {
            add(instructModeTemplate.assistantMessagePrefix)
        }
    }
}
