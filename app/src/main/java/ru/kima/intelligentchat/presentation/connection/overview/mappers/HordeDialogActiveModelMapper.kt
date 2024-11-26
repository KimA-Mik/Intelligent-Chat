package ru.kima.intelligentchat.presentation.connection.overview.mappers

import ru.kima.intelligentchat.domain.preferences.horde.model.HordeModelInfo
import ru.kima.intelligentchat.presentation.connection.overview.model.HordeDialogActiveModel

fun HordeModelInfo.toDialogActiveModel(selected: Boolean): HordeDialogActiveModel {
    val details =
        "(ETA: ${this.eta}s, Speed: ${this.performance}, Queue: ${this.queued}, Workers: ${this.count})"
    return HordeDialogActiveModel(
        name = name,
        selected = selected,
        details = details
    )
}