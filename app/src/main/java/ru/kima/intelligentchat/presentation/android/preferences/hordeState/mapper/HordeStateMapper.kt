package ru.kima.intelligentchat.presentation.android.preferences.hordeState.mapper

import ru.kima.intelligentchat.domain.preferences.horde.model.HordeState
import ru.kima.intelligentchat.presentation.android.preferences.hordeState.HordeStateSchema

fun HordeStateSchema.toHordeState(): HordeState {
    return HordeState(
        apiToken = apiToken,
        contextToWorker = contextToWorker,
        responseToWorker = responseToWorker,
        trustedWorkers = trustedWorkers,
        userName = userName,
        userId = userId,
        userContextSize = userContextSize,
        actualContextSize = actualContextSize,
        userResponseLength = userResponseLength,
        actualResponseLength = actualResponseLength,
        modelsInfo = modelsInfo.mapValues { it.value.toModelInfo() },
        selectedModels = selectedModels,
        selectedPreset = selectedPreset,
        generationId = generationId,
    )
}