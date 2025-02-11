package ru.kima.intelligentchat.presentation.android.preferences.advancedFormatting

import android.content.Context
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.preferences.advancedFormatting.AdvancedFormattingRepository

class AdvancedFormattingRepositoryImpl(context: Context) : AdvancedFormattingRepository {
    private val dataStore = context.advancedFormattingDataStore
    override fun preferences() = dataStore.data.map { it.toAdvancedFormatting() }
    override suspend fun updateSelectedContextTemplate(id: Long) {
        updateData {
            it.copy(
                selectedContextTemplate = id
            )
        }
    }

    override suspend fun updateSelectedInstructModeTemplate(id: Long) {
        updateData {
            it.copy(
                selectedInstructModeTemplate = id
            )
        }
    }

    private suspend fun updateData(
        transform: suspend (AdvancedFormattingSchema) -> AdvancedFormattingSchema
    ): AdvancedFormattingSchema {
        return dataStore.updateData(transform)
    }
}