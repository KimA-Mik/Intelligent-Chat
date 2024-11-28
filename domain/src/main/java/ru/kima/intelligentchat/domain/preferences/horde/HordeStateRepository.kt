package ru.kima.intelligentchat.domain.preferences.horde

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.preferences.horde.model.HordeModelInfo
import ru.kima.intelligentchat.domain.preferences.horde.model.HordeState

interface HordeStateRepository {
    fun hordeState(): Flow<HordeState>
    suspend fun selectPreset(presetId: Long)
    suspend fun updateHordeModelInfo(modelsInfo: Map<String, HordeModelInfo>)
    suspend fun updateActualGenerationDetails(contextSize: Int = -1, responseLength: Int = -1)
    suspend fun updateUserGenerationDetails(contextSize: Int = -1, responseLength: Int = -1)
    suspend fun selectModels(models: List<String>)
    suspend fun updateContextToWorker(contextToWorker: Boolean)
    suspend fun updateResponseToWorker(responseToWorker: Boolean)
    suspend fun updateTrustedWorkers(trustedWorkers: Boolean)
    suspend fun updateUserData(apiToken: String, userName: String, userId: Int)
    suspend fun updateGenerationId(generationId: String?)
}