package ru.kima.intelligentchat.data.migrations

import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec

@RenameTable(
    fromTableName = "instruct_mode_templates_table_name",
    toTableName = "instruct_mode_templates"
)
class Migration4_5 : AutoMigrationSpec
