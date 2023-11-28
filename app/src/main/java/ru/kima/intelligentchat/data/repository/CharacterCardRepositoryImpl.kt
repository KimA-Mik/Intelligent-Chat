package ru.kima.intelligentchat.data.repository

import android.content.Context
import androidx.room.Room
import ru.kima.intelligentchat.data.local.DATABASE_NAME
import ru.kima.intelligentchat.data.local.dataSource.ImageStorage
import ru.kima.intelligentchat.data.local.database.Database
import ru.kima.intelligentchat.data.local.entities.CharacterCardEntity
import ru.kima.intelligentchat.domain.model.CharacterCard
import ru.kima.intelligentchat.domain.repository.CharacterCardRepository

class CharacterCardRepositoryImpl(context: Context, private val imageStorage: ImageStorage) :
    CharacterCardRepository {
    private val database: Database = Room
        .databaseBuilder(
            context,
            Database::class.java,
            DATABASE_NAME
        )
        .build()

    override suspend fun getCharactersCards() =
        database.characterCardDao().selectCharacterCards().map { it.toCharacterCard(imageStorage) }

    override suspend fun getCharacterCard(id: Int) =
        database.characterCardDao().selectCharacterCard(id).toCharacterCard(imageStorage)

    override suspend fun putCharacterCard(characterCard: CharacterCard) {
        val entity = CharacterCardEntity.fromCharacterCard(characterCard)
        database.characterCardDao().insertCharacterCard(entity)
    }

    override suspend fun updateCharacterCard(characterCard: CharacterCard) {
        val entity = CharacterCardEntity.fromCharacterCard(characterCard)
        database.characterCardDao().updateCharacterCard(entity)
    }
}