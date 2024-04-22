package ru.kima.intelligentchat.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.MESSAGES_TABLE_NAME
import ru.kima.intelligentchat.data.chat.dto.MessageWithSwipesDto
import ru.kima.intelligentchat.data.chat.entities.MessageEntity

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(messageEntity: MessageEntity): Long

    @Update
    suspend fun updateMassage(messageEntity: MessageEntity)

    @Update
    suspend fun updateMessages(messages: List<MessageEntity>)

    @Transaction
    @Query("SELECT * FROM $MESSAGES_TABLE_NAME WHERE chat_id=:chatId ORDER By `index`")
    fun chatWithMessages(chatId: Long): Flow<List<MessageWithSwipesDto>>

    @Query("SELECT * FROM $MESSAGES_TABLE_NAME WHERE chat_id=:chatId ORDER By `index`")
    fun chatMessages(chatId: Long): Flow<List<MessageEntity>>

    @Query("DELETE FROM $MESSAGES_TABLE_NAME WHERE message_id = :messageId")
    suspend fun deleteMessage(messageId: Long): Int

    @Query("DELETE FROM $MESSAGES_TABLE_NAME WHERE message_id IN (:messageIds)")
    suspend fun deleteMessages(messageIds: List<Long>): Int

    @Query("DELETE FROM $MESSAGES_TABLE_NAME WHERE chat_id = :chatId")
    suspend fun deleteMessagesForChat(chatId: Long): Int
}