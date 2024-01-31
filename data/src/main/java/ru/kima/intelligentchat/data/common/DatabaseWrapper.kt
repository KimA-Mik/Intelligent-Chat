package ru.kima.intelligentchat.data.common

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import ru.kima.intelligentchat.data.DATABASE_NAME
import ru.kima.intelligentchat.data.Database
import ru.kima.intelligentchat.data.kobold.config.KoboldConfigEntity

class DatabaseWrapper(context: Context) {
    @OptIn(ExperimentalSerializationApi::class, DelicateCoroutinesApi::class)
    private val callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            GlobalScope.launch {
                val inputStream = context.assets.open("kobold_configs.json")
                val configs: List<KoboldConfigEntity> = Json.decodeFromStream(inputStream)
                inputStream.close()

                configs.forEach { preset ->
                    database
                        .koboldConfigDao()
                        .insert(preset)
                }
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