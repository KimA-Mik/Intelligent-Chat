package ru.kima.intelligentchat.domain.messaging.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.core.common.API_TYPE
import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.messaging.model.MessagingConfig

class LoadMessagingConfigUseCase(
    private val handler: HordeStateHandler
) {
    suspend operator fun invoke(
        apiType: API_TYPE,
        senderType: SenderType,
        personaName: String,
        cardName: String
    ) = when (apiType) {
        API_TYPE.KOBOLD_AI -> loadHordeConfig(senderType, personaName, cardName)
        API_TYPE.HORDE -> loadHordeConfig(senderType, personaName, cardName)
    }


    //TODO: Properly handle stop-sequences
    private suspend fun loadHordeConfig(
        senderType: SenderType,
        personaName: String,
        cardName: String
    ): MessagingConfig {
        val state = handler.data.first()
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