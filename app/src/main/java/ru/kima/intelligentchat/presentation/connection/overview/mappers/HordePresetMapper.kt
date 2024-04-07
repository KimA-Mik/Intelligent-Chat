package ru.kima.intelligentchat.presentation.connection.overview.mappers

import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset
import ru.kima.intelligentchat.presentation.connection.overview.model.HordePreset


fun KoboldPreset.toHordePreset(): HordePreset {
    return HordePreset(
        id = id,
        name = name,
    )
}