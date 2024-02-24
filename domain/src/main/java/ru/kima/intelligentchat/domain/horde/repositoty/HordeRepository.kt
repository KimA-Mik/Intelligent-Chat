package ru.kima.intelligentchat.domain.horde.repositoty

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.horde.model.ActiveModel
import ru.kima.intelligentchat.domain.horde.model.UserInfo

interface HordeRepository {
    suspend fun heartbeat(): Resource<Unit>
    suspend fun findUser(apiKey: String): Resource<UserInfo>
    suspend fun activeModels(): Resource<List<ActiveModel>>
    fun connectionState(): Flow<Boolean>
}