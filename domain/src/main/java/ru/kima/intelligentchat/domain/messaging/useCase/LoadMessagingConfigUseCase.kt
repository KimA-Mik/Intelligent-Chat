package ru.kima.intelligentchat.domain.messaging.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.common.ApiType
import ru.kima.intelligentchat.domain.messaging.model.MessagingConfig
import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class LoadMessagingConfigUseCase(
    private val hordeStateRepository: HordeStateRepository,
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
        return MessagingConfig(
            maxResponseLength = state.actualResponseLength,
            maxContextLength = state.actualContextSize,
            stopSequence = getStopSequence(senderType, personaName, cardName)
        )
    }

    private fun getStopSequence(
        senderType: SenderType,
        personaName: String,
        cardName: String
    ): List<String> {
        return when (senderType) {
            SenderType.Character -> listOf("\n$personaName:")
            SenderType.Persona -> listOf("$cardName:")
        }
    }
}