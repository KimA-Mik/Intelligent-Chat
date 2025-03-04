package ru.kima.intelligentchat.data.chat.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.data.CHATS_TABLE_NAME
import ru.kima.intelligentchat.data.MESSAGES_TABLE_NAME
import ru.kima.intelligentchat.data.SWIPE_TABLE_NAME
import ru.kima.intelligentchat.data.chat.dto.ChatWithMessagesDto
import ru.kima.intelligentchat.data.chat.dto.MessageWithSwipesDto
import ru.kima.intelligentchat.data.chat.dto.SimpleChatWithMessagesDto
import ru.kima.intelligentchat.data.chat.entities.ChatEntity
import ru.kima.intelligentchat.data.chat.entities.MessageEntity
import ru.kima.intelligentchat.data.chat.entities.SwipeEntity

@Dao
interface ChatDao {
    @Insert
    suspend fun insertChat(chatEntity: ChatEntity): Long

    @Update
    suspend fun updateChat(chatEntity: ChatEntity)

    @Query("SELECT * FROM $CHATS_TABLE_NAME WHERE chat_id = :chatId")
    fun subscribeToChat(chatId: Long): Flow<ChatEntity?>

    @Delete
    suspend fun deleteChat(chat: ChatEntity)

    @Transaction
    @Query("SELECT * FROM $CHATS_TABLE_NAME WHERE card_id = :cardId")
    fun subscribeToCardChats(cardId: Long): Flow<List<SimpleChatWithMessagesDto>>

    @Transaction
    @Query("SELECT * FROM $CHATS_TABLE_NAME WHERE card_id IN (:cardIds)")
    fun getChatsForCardIds(cardIds: List<Long>): List<ChatWithMessagesDto>

    @Insert
    suspend fun insertMessage(messageEntity: MessageEntity): Long

    @Insert
    suspend fun insertMessages(messages: List<MessageEntity>): LongArray

    @Update
    suspend fun updateMassage(messageEntity: MessageEntity)

    @Update
    suspend fun updateMessages(messages: List<MessageEntity>): Int

    @Transaction
    @Query("SELECT * FROM $MESSAGES_TABLE_NAME WHERE message_id=:messageId")
    suspend fun getFullMessage(messageId: Long): MessageWithSwipesDto?

    @Query("SELECT * FROM $MESSAGES_TABLE_NAME WHERE message_id=:messageId")
    suspend fun getMessage(messageId: Long): MessageEntity?

    @Transaction
    @Query("SELECT * FROM $MESSAGES_TABLE_NAME WHERE chat_id=:chatId AND deleted=0 ORDER By `index`")
    fun chatWithMessages(chatId: Long): Flow<List<MessageWithSwipesDto>>

    @Query("SELECT * FROM $MESSAGES_TABLE_NAME WHERE chat_id=:chatId AND deleted=0 ORDER By `index`")
    fun chatMessages(chatId: Long): Flow<List<MessageEntity>>

    @Transaction
    @Query("SELECT * FROM $MESSAGES_TABLE_NAME WHERE deleted=1")
    suspend fun markedMessages(): List<MessageWithSwipesDto>

    @Delete
    suspend fun deleteMessages(messages: List<MessageEntity>)

    @Insert
    suspend fun insertSwipe(swipeEntity: SwipeEntity): Long

    @Insert
    suspend fun insertSwipes(swipes: List<SwipeEntity>): LongArray

    @Update
    suspend fun updateSwipe(swipeEntity: SwipeEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateSwipes(swipes: List<SwipeEntity>)

    @Query("SELECT * FROM $SWIPE_TABLE_NAME WHERE message_id in (:messagesIds)")
    fun swipesForMessages(messagesIds: List<Long>): Flow<List<SwipeEntity>>

    @Query(
        "SELECT * FROM $SWIPE_TABLE_NAME WHERE message_id in " +
                "(SELECT message_id FROM $MESSAGES_TABLE_NAME WHERE chat_id = :chatId)"
    )
    fun swipesForChat(chatId: Long): Flow<List<SwipeEntity>>

    @Query("SELECT * FROM $SWIPE_TABLE_NAME WHERE  swipe_id = :swipeId")
    suspend fun getSwipe(swipeId: Long): SwipeEntity

    @Query("SELECT * FROM $SWIPE_TABLE_NAME WHERE  deleted = 1")
    suspend fun markedSwipes(): List<SwipeEntity>

    @Delete
    suspend fun deleteSwipes(swipes: List<SwipeEntity>)

    @Transaction
    suspend fun deleteMessagesDto(dtos: List<MessageWithSwipesDto>) {
        val messages = dtos.map { it.message }
        val swipes = dtos.flatMap { it.swipes }
        deleteMessages(messages)
        deleteSwipes(swipes)
    }

    @Transaction
    suspend fun updateMessageDto(dto: MessageWithSwipesDto) {
        updateMassage(dto.message)
        updateSwipes(dto.swipes)
    }

    @Transaction
    suspend fun deleteChat(chatWithMessagesDto: ChatWithMessagesDto) {
        deleteChat(chatWithMessagesDto.chat)
        deleteMessagesDto(chatWithMessagesDto.messages)
    }

    @Transaction
    suspend fun deleteChatsTransaction(chats: List<ChatWithMessagesDto>) {
        chats.forEach { deleteChat(it) }
    }

    @Transaction
    suspend fun copyChat(chatWithMessagesDto: ChatWithMessagesDto): Long {
        val chat = chatWithMessagesDto.chat.copy(chatId = 0L)
        val chatId = insertChat(chat)
        if (chatWithMessagesDto.messages.isEmpty()) return chatId

        val swipes = mutableListOf<SwipeEntity>()
        val messageEntities = chatWithMessagesDto.messages.mapIndexed { index, dto ->
            dto.message.copy(
                messageId = 0L, chatId = chatId, index = index + 1
            )
        }

        val messageIds = insertMessages(messageEntities)
        for (i in messageIds.indices) {
            val messageId = messageIds[i]
            for (swipe in chatWithMessagesDto.messages[i].swipes) {
                swipes.add(swipe.copy(swipeId = 0L, messageId = messageId))
            }
        }

        insertSwipes(swipes)
        return chatId
    }
}