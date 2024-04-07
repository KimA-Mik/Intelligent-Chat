package ru.kima.intelligentchat.presentation.connection.overview

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.core.common.API_TYPE
import ru.kima.intelligentchat.presentation.connection.overview.model.HordeDialogActiveModel
import ru.kima.intelligentchat.presentation.connection.overview.model.HordeModelsWrapper
import ru.kima.intelligentchat.presentation.connection.overview.model.HordePresetsWrapper

@Immutable
data class ConnectionOverviewState(
    val selectedApiType: API_TYPE = API_TYPE.HORDE,
    val hordeFragmentState: HordeFragmentState = HordeFragmentState()
) {
    @Immutable
    data class HordeFragmentState(
        val currentApiToken: String = String(),
        val showApiToken: Boolean = false,
        val contextToWorker: Boolean = true,
        val responseToWorker: Boolean = true,
        val trustedWorkers: Boolean = false,
        val userName: String = String(),
        val contextSize: Int = 1024,
        val responseLength: Int = 256,
        val showSelectHordeModelsDialog: Boolean = false,
        val selectedModelsWrapper: HordeModelsWrapper = HordeModelsWrapper(),
        val dialogSelectedModels: List<HordeDialogActiveModel> = emptyList(),
        val presetsWrapper: HordePresetsWrapper = HordePresetsWrapper(),
    )
}

