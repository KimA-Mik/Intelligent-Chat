package ru.kima.intelligentchat.domain.messaging.instructMode

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.messaging.instructMode.model.InstructModeTemplate

interface InstructModeTemplateRepository {
    suspend fun insert(template: InstructModeTemplate): Long
    suspend fun update(template: InstructModeTemplate)

    fun subscribeToAll(): Flow<List<InstructModeTemplate>>
    suspend fun getById(id: Long): InstructModeTemplate?
}