package ru.kima.intelligentchat.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kima.intelligentchat.data.card.dao.CardTagDao
import ru.kima.intelligentchat.data.card.dao.CharacterCardDao
import ru.kima.intelligentchat.data.card.dao.TagDao
import ru.kima.intelligentchat.data.card.entities.CardTagEntity
import ru.kima.intelligentchat.data.card.entities.CharacterCardEntity
import ru.kima.intelligentchat.data.card.entities.TagEntity
import ru.kima.intelligentchat.data.persona.PersonaDao
import ru.kima.intelligentchat.data.persona.PersonaEntity

@Database(
    entities = [
        CharacterCardEntity::class,
        TagEntity::class,
        CardTagEntity::class,
        PersonaEntity::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun characterCardDao(): CharacterCardDao
    abstract fun tagDao(): TagDao
    abstract fun cardTagDao(): CardTagDao
    abstract fun personaDao(): PersonaDao
}