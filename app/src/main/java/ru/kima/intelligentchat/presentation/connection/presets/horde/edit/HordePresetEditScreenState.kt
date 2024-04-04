package ru.kima.intelligentchat.presentation.connection.presets.horde.edit

import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset

sealed interface HordePresetEditScreenState {
    data class Preset(val preset: KoboldPreset = KoboldPreset()) : HordePresetEditScreenState
    data object NoPreset : HordePresetEditScreenState
}