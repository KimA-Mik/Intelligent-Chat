package ru.kima.intelligentchat.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.MESSAGES_TABLE_NAME
import ru.kima.intelligentchat.data.SWIPE_TABLE_NAME
import ru.kima.intelligentchat.data.chat.entities.SwipeEntity

@Dao
interface SwipeDao {
    @Insert
    suspend fun insertSwipe(swipeEntity: SwipeEntity): Long

    @Update
    suspend fun updateSwipe(swipeEntity: SwipeEntity)

    @Query("SELECT * FROM $SWIPE_TABLE_NAME WHERE message_id in (:messagesIds)")
    fun swipesForMessages(messagesIds: List<Long>): Flow<List<SwipeEntity>>

    @Query(
        "SELECT * FROM $SWIPE_TABLE_NAME WHERE message_id in " +
                "(SELECT message_id FROM $MESSAGES_TABLE_NAME WHERE chat_id = :chatId)"
    )
    fun swipesForChat(chatId: Long): Flow<List<SwipeEntity>>
}