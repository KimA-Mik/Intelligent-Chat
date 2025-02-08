package ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate

interface InstructModeTemplateRepository {
    suspend fun insert(template: InstructModeTemplate): Long
    suspend fun update(template: InstructModeTemplate)
    suspend fun delete(template: InstructModeTemplate)

    fun subscribeToAll(): Flow<List<InstructModeTemplate>>
    suspend fun getById(id: Long): InstructModeTemplate?
}