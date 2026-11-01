package ru.kima.intelligentchat.data.kobold.preset.repository

import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.kobold.preset.mappers.toKoboldPreset
import ru.kima.intelligentchat.data.kobold.preset.mappers.toTextCompletionPresetEntity
import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset
import ru.kima.intelligentchat.domain.presets.textCompletion.repository.TextCompletionPresetRepository

class TextCompletionPresetRepositoryImpl(
    wrapper: DatabaseWrapper
) : TextCompletionPresetRepository {
    private val dao = wrapper.database.textCompletionPresetDao()

    override fun subscribeToPresets() = dao.subscribeToAll().map { presets ->
        presets.map { it.toKoboldPreset() }
    }

    override suspend fun getPreset(id: Long) = dao.select(id)?.toKoboldPreset()

    override suspend fun updatePreset(preset: KoboldPreset) {
        dao.update(preset.toTextCompletionPresetEntity())
    }
}
