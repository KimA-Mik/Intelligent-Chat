package ru.kima.intelligentchat.data.chat.advancedFormatting.contextTemplate

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.ContextTemplateRepository
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate

class ContextTemplateRepositoryImpl(
    wrapper: DatabaseWrapper
) : ContextTemplateRepository {
    private val dao = wrapper.database.contextTemplateDao()
    override suspend fun insert(contextTemplate: ContextTemplate): Long {
        return dao.insert(contextTemplate.toEntity())
    }

    override suspend fun update(contextTemplate: ContextTemplate): Int {
        return dao.update(contextTemplate.toEntity())
    }

    override suspend fun delete(contextTemplate: ContextTemplate): Int {
        return dao.delete(contextTemplate.toEntity())
    }

    override suspend fun getById(id: Long): ContextTemplate? {
        return dao.selectById(id)?.toModel()
    }

    override fun subscribeToAll(): Flow<List<ContextTemplate>> {
        return dao.subscribeToAll().map { it.map(ContextTemplateEntity::toModel) }
    }
}