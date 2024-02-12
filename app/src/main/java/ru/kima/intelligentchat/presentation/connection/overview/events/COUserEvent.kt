package ru.kima.intelligentchat.presentation.connection.overview.events

import ru.kima.intelligentchat.core.common.API_TYPE

interface COUserEvent {
    data class UpdateSelectedApi(val apiType: API_TYPE) : COUserEvent
}