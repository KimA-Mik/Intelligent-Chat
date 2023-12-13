package ru.kima.intelligentchat.data.common

import android.content.Context
import androidx.room.Room
import ru.kima.intelligentchat.data.DATABASE_NAME
import ru.kima.intelligentchat.data.Database

class DatabaseWrapper(context: Context) {
    val database: Database = Room
        .databaseBuilder(
            context,
            Database::class.java,
            DATABASE_NAME
        )
        .build()
}