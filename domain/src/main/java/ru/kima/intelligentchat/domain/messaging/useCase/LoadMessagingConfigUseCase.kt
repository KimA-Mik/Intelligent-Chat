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
        ApiType.KOBOLD_AI, ApiType.HORDE ->
            loadConfig(senderType, personaName, cardName, useUserGenerationDetails = false)

        ApiType.TEXT_COMPLETION ->
            loadConfig(senderType, personaName, cardName, useUserGenerationDetails = true)
    }

    //TODO: Properly handle stop-sequences
    private suspend fun loadConfig(
        senderType: SenderType,
        personaName: String,
        cardName: String,
        useUserGenerationDetails: Boolean
    ): MessagingConfig {
        val state = hordeStateRepository.hordeState().first()
        val instructModeTemplate = getSelectedInstructTemplate()
        return MessagingConfig(
            maxResponseLength = if (useUserGenerationDetails) {
                state.userResponseLength
            } else {
                state.actualResponseLength
            },
            maxContextLength = if (useUserGenerationDetails) {
                state.userContextSize
            } else {
                state.actualContextSize
            },
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
