package ru.kima.intelligentchat.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kima.intelligentchat.data.local.dao.CardTagDao
import ru.kima.intelligentchat.data.local.dao.CharacterCardDao
import ru.kima.intelligentchat.data.local.dao.TagDao
import ru.kima.intelligentchat.data.local.entities.CardTagEntity
import ru.kima.intelligentchat.data.local.entities.CharacterCardEntity
import ru.kima.intelligentchat.data.local.entities.TagEntity

@Database(
    entities = [
        CharacterCardEntity::class,
        TagEntity::class,
        CardTagEntity::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun characterCardDao(): CharacterCardDao
    abstract fun tagDao(): TagDao
    abstract fun cardTagDao(): CardTagDao
}