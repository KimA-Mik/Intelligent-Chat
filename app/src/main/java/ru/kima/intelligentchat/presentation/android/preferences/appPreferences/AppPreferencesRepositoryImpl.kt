package ru.kima.intelligentchat.presentation.android.preferences.appPreferences

import android.content.Context
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.common.ApiType
import ru.kima.intelligentchat.domain.preferences.Preference
import ru.kima.intelligentchat.domain.preferences.app.AppPreferencesRepository
import ru.kima.intelligentchat.presentation.android.preferences.ICPreference

class AppPreferencesRepositoryImpl(context: Context) : AppPreferencesRepository {
    private val store = context.preferencesDataStore

    override fun preferences() = store.data.map {
        it.toPreferences()
    }

    override fun showImages(): Preference<Boolean> = ICPreference(
        initialValue = false,
        flow = store.data.map { it.showImages },
        setter = { value ->
            updateData { it.copy(showImages = value) }
        }
    )

    override suspend fun updateSelectedPersona(id: Long) {
        updateData {
            it.copy(
                selectedPersonaId = id
            )
        }
    }

    override suspend fun updateSelectedAip(apiType: ApiType) {
        updateData {
            it.copy(
                selectedApiType = apiType
            )
        }
    }

    override suspend fun updateGenerationPending(generationPending: Boolean) {
        updateData {
            it.copy(
                generationPending = generationPending
            )
        }
    }

    private suspend fun updateData(
        transform: suspend (AppPreferencesSchema) -> AppPreferencesSchema
    ): AppPreferencesSchema {
        return store.updateData(transform)
    }
}