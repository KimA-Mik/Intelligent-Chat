package ru.kima.intelligentchat.presentation.android.preferences.appPreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.domain.common.ApiType
import ru.kima.intelligentchat.domain.preferences.app.AppPreferences

@Serializable
data class AppPreferencesSchema(
    val selectedPersonaId: Long = 0,
    val selectedApiType: ApiType = ApiType.HORDE,
    val generationPending: Boolean = false
)

val Context.preferencesDataStore: DataStore<AppPreferencesSchema> by dataStore(
    fileName = "preferences.pb",
    serializer = AppPreferencesSerialize
)

fun AppPreferencesSchema.toPreferences(): AppPreferences {
    return AppPreferences(
        selectedPersonaId = selectedPersonaId,
        selectedApiType = selectedApiType,
        generationPending = generationPending,
    )
}