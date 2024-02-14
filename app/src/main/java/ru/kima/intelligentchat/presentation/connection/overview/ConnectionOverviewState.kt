package ru.kima.intelligentchat.presentation.connection.overview

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.core.common.API_TYPE

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
        val trustedWorkers: Boolean = false
    )
}

