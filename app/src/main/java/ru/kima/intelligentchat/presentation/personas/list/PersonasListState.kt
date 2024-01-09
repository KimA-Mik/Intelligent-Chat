package ru.kima.intelligentchat.presentation.personas.list

import android.graphics.Bitmap
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.kima.intelligentchat.presentation.personas.list.model.PersonaItem

@Stable
data class PersonasListState(
    val personas: ImmutableList<PersonaItem> = persistentListOf(),
    val thumbnails: ImmutableList<Bitmap?> = persistentListOf(),
    val query: String = String()
)
