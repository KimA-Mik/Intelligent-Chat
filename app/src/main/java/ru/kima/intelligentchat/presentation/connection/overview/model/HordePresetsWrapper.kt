package ru.kima.intelligentchat.presentation.connection.overview.model

import androidx.compose.runtime.Immutable

@Immutable
data class HordePresetsWrapper(
    val preset: HordePreset = HordePreset(1, "0_o"),
    val presets: List<HordePreset> = emptyList()
)