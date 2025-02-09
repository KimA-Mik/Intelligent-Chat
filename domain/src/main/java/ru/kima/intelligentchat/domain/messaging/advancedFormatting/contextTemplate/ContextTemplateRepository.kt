package ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate

interface ContextTemplateRepository {
    suspend fun insert(contextTemplate: ContextTemplate): Long
    suspend fun update(contextTemplate: ContextTemplate): Int
    suspend fun delete(contextTemplate: ContextTemplate): Int
    suspend fun getById(id: Long): ContextTemplate?
    fun subscribeToAll(): Flow<List<ContextTemplate>>
}