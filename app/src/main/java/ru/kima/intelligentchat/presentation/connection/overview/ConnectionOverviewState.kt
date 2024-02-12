package ru.kima.intelligentchat.presentation.connection.overview

import ru.kima.intelligentchat.core.common.API_TYPE

data class ConnectionOverviewState(
    val selectedApiType: API_TYPE = API_TYPE.HORDE
)