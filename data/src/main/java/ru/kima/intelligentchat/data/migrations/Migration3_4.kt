package ru.kima.intelligentchat.data.migrations

import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.kima.intelligentchat.data.Database
import ru.kima.intelligentchat.data.INSTRUCT_MODE_TEMPLATES_TABLE_NAME

class Migration3_4 : AutoMigrationSpec {
    override fun onPostMigrate(db: SupportSQLiteDatabase) {
//        val default = InstructModeTemplate.default(
//            id = 1,
//            name = "Default"
//        ).toEntity()

//        db.execSQL(
//            "INSERT OR ABORT INTO $INSTRUCT_MODE_TEMPLATES_TABLE_NAME " +
//                    "(`id`,`name`,`include_name_policy`,`wrap_sequences_with_new_line`,`user_message_prefix`,`user_message_suffix`,`assistant_message_prefix`,`assistant_message_suffix`,`system_same_as_user`,`first_assistant_prefix`,`last_assistant_prefix`,`first_user_prefix`,`last_user_prefix`) " +
//                    "VALUES " +
//                    "(${default.id},${default.name},${default.includeNamePolicy},${default.wrapSequencesWithNewLine},${default.userMessagePrefix},${default.userMessageSuffix},${default.assistantMessagePrefix},${default.assistantMessageSuffix},?,?,?,?,?)"
//        )

        db.execSQL("INSERT OR ABORT INTO $INSTRUCT_MODE_TEMPLATES_TABLE_NAME DEFAULT VALUES")

        (db as Database).chatDao()
    }
}
