package ru.kima.intelligentchat.domain.horde.repositoty

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.horde.model.ActiveModel
import ru.kima.intelligentchat.domain.horde.model.GenerationInput
import ru.kima.intelligentchat.domain.horde.model.HordeAsyncRequest
import ru.kima.intelligentchat.domain.horde.model.HordeWorker
import ru.kima.intelligentchat.domain.horde.model.UserInfo

interface HordeRepository {
    suspend fun heartbeat(): Resource<Unit>
    suspend fun findUser(apiKey: String): Resource<UserInfo>
    suspend fun activeModels(): Resource<List<ActiveModel>>
    suspend fun workers(): Resource<List<HordeWorker>>
    suspend fun requestGeneration(
        apiKey: String,
        generationInput: GenerationInput
    ): Resource<HordeAsyncRequest>
    fun connectionState(): Flow<Boolean>
}