package ru.kima.intelligentchat.presentation.connection.overview.model

import androidx.compose.runtime.Immutable

@Immutable
data class HordeDialogActiveModel(
    val name: String = String(),
    val details: String = String(),
    val selected: Boolean = false
)
