package ru.kima.intelligentchat.data.chat.instructMode

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.domain.messaging.instructMode.InstructModeTemplateRepository
import ru.kima.intelligentchat.domain.messaging.instructMode.model.InstructModeTemplate

class InstructModeTemplateRepositoryImpl(
    wrapper: DatabaseWrapper
) : InstructModeTemplateRepository {
    private val dao = wrapper.database.instructModeTemplateDao()
    override suspend fun insert(template: InstructModeTemplate) {
        dao.insert(template.toEntity())
    }

    override suspend fun update(template: InstructModeTemplate) {
        dao.update(template.toEntity())
    }

    override fun subscribeToAll(): Flow<List<InstructModeTemplate>> {
        return dao.subscribeToAll().map {
            it.map(InstructModeTemplateEntity::toDomain)
        }
    }

    override suspend fun getById(id: Long): InstructModeTemplate? {
        return dao.get(id)?.toDomain()
    }
}