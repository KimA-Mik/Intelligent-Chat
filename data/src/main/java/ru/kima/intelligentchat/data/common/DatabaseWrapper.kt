package ru.kima.intelligentchat.data.common

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import ru.kima.intelligentchat.data.DATABASE_NAME
import ru.kima.intelligentchat.data.Database
import ru.kima.intelligentchat.data.chat.advancedFormatting.instructMode.toEntity
import ru.kima.intelligentchat.data.kobold.preset.entities.KoboldPresetEntity
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate

class DatabaseWrapper(context: Context) {
    @OptIn(ExperimentalSerializationApi::class, DelicateCoroutinesApi::class)
    private val callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val inputStream = context.assets.open("kobold_configs.json")
                val configs: List<KoboldPresetEntity> = Json.decodeFromStream(inputStream)
                inputStream.close()

                configs.forEach { preset ->
                    database
                        .koboldPresetDao()
                        .insert(preset)
                }

                database.instructModeTemplateDao().insert(
                    //TODO: Localize `default` name
                    InstructModeTemplate.default(name = "Default").toEntity()
                )
            }
        }
    }


    val database: Database = Room
        .databaseBuilder(
            context,
            Database::class.java,
            DATABASE_NAME
        )
        .addCallback(callback)
        .build()
}