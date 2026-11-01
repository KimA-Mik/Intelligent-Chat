package ru.kima.intelligentchat.domain.presets.textCompletion.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset

interface TextCompletionPresetRepository {
    fun subscribeToPresets(): Flow<List<KoboldPreset>>
    suspend fun getPreset(id: Long): KoboldPreset?
    suspend fun updatePreset(preset: KoboldPreset)
}
