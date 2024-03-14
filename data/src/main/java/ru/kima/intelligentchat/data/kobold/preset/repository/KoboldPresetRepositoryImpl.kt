package ru.kima.intelligentchat.data.kobold.preset.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.kobold.preset.entities.KoboldPresetEntity
import ru.kima.intelligentchat.data.kobold.preset.mappers.toEntity
import ru.kima.intelligentchat.data.kobold.preset.mappers.toKoboldPreset
import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset
import ru.kima.intelligentchat.domain.presets.kobold.repository.KoboldPresetRepository

class KoboldPresetRepositoryImpl(
    wrapper: DatabaseWrapper
) : KoboldPresetRepository {
    private val dao = wrapper.database.koboldPresetDao()
    override fun subscribeToPresets(): Flow<List<KoboldPreset>> {
        return dao
            .subscribeToAll()
            .map { list ->
                list.map(KoboldPresetEntity::toKoboldPreset)
            }
    }

    override suspend fun getPreset(id: Long): KoboldPreset? {
        return dao.select(id)?.toKoboldPreset()
    }

    override suspend fun updatePreset(preset: KoboldPreset) {
        dao.update(preset.toEntity())
    }
}