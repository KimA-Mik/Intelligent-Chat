package ru.kima.intelligentchat.data.migrations

import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.kima.intelligentchat.data.INSTRUCT_MODE_TEMPLATES_TABLE_NAME

class Migration3_4 : AutoMigrationSpec {
    override fun onPostMigrate(db: SupportSQLiteDatabase) {
        db.execSQL("INSERT OR ABORT INTO $INSTRUCT_MODE_TEMPLATES_TABLE_NAME DEFAULT VALUES")
    }
}
