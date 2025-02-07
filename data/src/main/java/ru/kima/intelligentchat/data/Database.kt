package ru.kima.intelligentchat.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kima.intelligentchat.data.card.dao.CardTagDao
import ru.kima.intelligentchat.data.card.dao.CharacterCardDao
import ru.kima.intelligentchat.data.card.dao.TagDao
import ru.kima.intelligentchat.data.card.entities.AltGreetingEntity
import ru.kima.intelligentchat.data.card.entities.CardTagEntity
import ru.kima.intelligentchat.data.card.entities.CharacterEntity
import ru.kima.intelligentchat.data.card.entities.TagEntity
import ru.kima.intelligentchat.data.chat.dao.ChatDao
import ru.kima.intelligentchat.data.chat.entities.ChatEntity
import ru.kima.intelligentchat.data.chat.entities.MessageEntity
import ru.kima.intelligentchat.data.chat.entities.SwipeEntity
import ru.kima.intelligentchat.data.chat.instructMode.InstructModeTemplateDao
import ru.kima.intelligentchat.data.chat.instructMode.InstructModeTemplateEntity
import ru.kima.intelligentchat.data.kobold.preset.dao.KoboldPresetDao
import ru.kima.intelligentchat.data.kobold.preset.entities.KoboldPresetEntity
import ru.kima.intelligentchat.data.migrations.Migration3_4
import ru.kima.intelligentchat.data.persona.PersonaDao
import ru.kima.intelligentchat.data.persona.PersonaEntity

@Database(
    entities = [
        CharacterEntity::class,
        TagEntity::class,
        CardTagEntity::class,
        PersonaEntity::class,
        AltGreetingEntity::class,
        KoboldPresetEntity::class,
        ChatEntity::class,
        MessageEntity::class,
        SwipeEntity::class,
        InstructModeTemplateEntity::class
    ],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4, spec = Migration3_4::class)
    ]
)
abstract class Database : RoomDatabase() {
    abstract fun characterCardDao(): CharacterCardDao
    abstract fun tagDao(): TagDao
    abstract fun cardTagDao(): CardTagDao
    abstract fun personaDao(): PersonaDao
    abstract fun koboldPresetDao(): KoboldPresetDao
    abstract fun chatDao(): ChatDao
    abstract fun instructModeTemplateDao(): InstructModeTemplateDao
}