package ru.kima.intelligentchat.data.migrations

import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration7_8 : AutoMigrationSpec {
    override fun onPostMigrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            INSERT INTO text_completion_preset (
                id, name, temperature, repetitionPenalty, repetitionPenaltyRange, topP, topA,
                topK, typical, tailFreeSampling, repetitionPenaltySlope, samplerOrder,
                mirostat, mirostatTau, mirostatEta, minP, grammar
            )
            SELECT
                id, name, temperature, repetitionPenalty, repetitionPenaltyRange, topP, topA,
                topK, typical, tailFreeSampling, repetitionPenaltySlope, samplerOrder,
                mirostat, mirostatTau, mirostatEta, minP, grammar
            FROM kobold_preset
            """.trimIndent()
        )
    }
}
