package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Swipe

interface SwipeRepository {
    fun subscribeSwipesForMessages(messagesId: List<Long>): Flow<List<Swipe>>
    fun subscribeSwipesForChart(chatId: Long): Flow<List<Swipe>>
    suspend fun getSwipe(swipeId: Long): Swipe
    suspend fun deleteSwipe(swipeId: Long): Boolean
    suspend fun deleteSwipesForMessage(messageId: Long): Boolean
    suspend fun deleteSwipesForMessages(messageIds: List<Long>): Boolean
    suspend fun createSwipe(messageId: Long, text: String): Long
    suspend fun updateSwipe(swipe: Swipe)
}