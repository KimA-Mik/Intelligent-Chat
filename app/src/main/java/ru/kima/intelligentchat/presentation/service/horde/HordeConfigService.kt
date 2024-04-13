package ru.kima.intelligentchat.presentation.service.horde

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.preferences.horde.useCase.GetHordePreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateActualGenerationDetailsUseCase
import kotlin.math.min

class HordeConfigService(
    hordePreferences: GetHordePreferencesUseCase,
    updateActualGenerationDetails: UpdateActualGenerationDetailsUseCase
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    init {
        scope.launch {
            val preferences = hordePreferences()
            preferences.collect { horde ->
                var contextSize = Int.MAX_VALUE
                var responseLength = Int.MAX_VALUE

                horde.selectedModels.forEach { modelName ->
                    val modelInfo = horde.modelsInfo[modelName] ?: return@forEach

                    if (modelInfo.maxContextSize < contextSize)
                        contextSize = modelInfo.maxContextSize

                    if (modelInfo.maxResponseLength < responseLength)
                        responseLength = modelInfo.maxResponseLength
                }
                if (contextSize == Int.MAX_VALUE) contextSize = 0
                if (responseLength == Int.MAX_VALUE) responseLength = 0

                contextSize =
                    if (horde.contextToWorker) min(contextSize, horde.userContextSize)
                    else horde.userContextSize

                responseLength =
                    if (horde.responseToWorker) min(responseLength, horde.userResponseLength)
                    else horde.userResponseLength

                updateActualGenerationDetails(
                    contextSize = contextSize,
                    responseLength = responseLength
                )
            }
        }
    }
}