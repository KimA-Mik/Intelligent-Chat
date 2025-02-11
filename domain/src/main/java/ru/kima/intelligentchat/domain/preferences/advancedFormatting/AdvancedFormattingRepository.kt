package ru.kima.intelligentchat.domain.preferences.advancedFormatting

import kotlinx.coroutines.flow.Flow

interface AdvancedFormattingRepository {
    fun preferences(): Flow<AdvancedFormatting>
    suspend fun updateSelectedContextTemplate(id: Long)
    suspend fun updateSelectedInstructModeTemplate(id: Long)
}