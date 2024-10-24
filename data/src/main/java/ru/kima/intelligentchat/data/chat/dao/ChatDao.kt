package ru.kima.intelligentchat.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.CARDS_TABLE_NAME
import ru.kima.intelligentchat.data.CHATS_TABLE_NAME
import ru.kima.intelligentchat.data.chat.dto.ChatWithMessagesDto
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

    @Transaction
    @Query("SELECT * FROM $CHATS_TABLE_NAME WHERE chat_id = (SELECT chat_id FROM $CARDS_TABLE_NAME WHERE card_id = :cardId)")
    fun subscribeToCardChat(cardId: Long): Flow<ChatWithMessagesDto?>
}