package ru.kima.intelligentchat.domain.presets.kobold.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset

interface KoboldPresetRepository {
    fun subscribeToPresets(): Flow<List<KoboldPreset>>
    suspend fun getPreset(id: Long): KoboldPreset?
    suspend fun updatePreset(preset: KoboldPreset)
}