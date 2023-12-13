package ru.kima.intelligentchat.core.preferences

import android.content.Context

class PreferencesHandler(context: Context) {
    private val store = context.preferencesDataStore
    val data = store.data
    suspend fun updateData(
        transform: suspend (AppPreferences) -> AppPreferences
    ): AppPreferences {
        return store.updateData(transform)
    }
}