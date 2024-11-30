package ru.kima.intelligentchat.presentation.android.preferences.appAppearance

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable

@Serializable
data class AppAppearanceSchema(
    val darkMode: AppAppearance.DarkMode = AppAppearance.DarkMode.SYSTEM
)

val Context.appearanceDataStore: DataStore<AppAppearanceSchema> by dataStore(
    fileName = "appearance.pb",
    serializer = AppAppearanceSerializer
)