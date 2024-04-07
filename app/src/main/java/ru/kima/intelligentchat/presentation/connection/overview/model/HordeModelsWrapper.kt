package ru.kima.intelligentchat.presentation.connection.overview.model

import androidx.compose.runtime.Immutable

@Immutable
data class HordeModelsWrapper(
    val selectedModels: List<String> = emptyList()
)