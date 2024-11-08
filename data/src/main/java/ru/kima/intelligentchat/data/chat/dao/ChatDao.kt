package ru.kima.intelligentchat.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.CHATS_TABLE_NAME
import ru.kima.intelligentchat.data.chat.entities.ChatEntity

@Dao
interface ChatDao {
    @Insert
    suspend fun insertChat(chatEntity: ChatEntity): Long

    @Update
    suspend fun updateChat(chatEntity: ChatEntity)

    @Query("SELECT * FROM $CHATS_TABLE_NAME WHERE chat_id = :chatId")
    fun subscribeToChat(chatId: Long): Flow<ChatEntity?>

    @Query("DELETE FROM $CHATS_TABLE_NAME WHERE chat_id = :chatId")
    suspend fun deleteChat(chatId: Long): Int
}