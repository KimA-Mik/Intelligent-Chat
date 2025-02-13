package ru.kima.intelligentchat.presentation.android.preferences.advancedFormatting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.domain.preferences.advancedFormatting.AdvancedFormatting

@Serializable
data class AdvancedFormattingSchema(
    val selectedContextTemplate: Long = 0L,
    val selectedInstructModeTemplate: Long = 0L
)

val Context.advancedFormattingDataStore: DataStore<AdvancedFormattingSchema> by dataStore(
    fileName = "advanced_formatting.pb",
    serializer = AdvancedFormattingSerializer
)

fun AdvancedFormattingSchema.toAdvancedFormatting(): AdvancedFormatting {
    return AdvancedFormatting(
        selectedContextTemplate = selectedContextTemplate,
        selectedInstructModeTemplate = selectedInstructModeTemplate
    )
}