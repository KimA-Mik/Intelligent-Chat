package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Swipe

interface SwipeRepository {
    fun subscribeSwipesForMessages(messagesId: List<Long>): Flow<List<Swipe>>
    fun subscribeSwipesForChart(chatId: Long): Flow<List<Swipe>>
    suspend fun getSwipe(swipeId: Long): Swipe
    suspend fun createSwipe(messageId: Long, text: String): Long
    suspend fun updateSwipe(swipe: Swipe)
    suspend fun getMarkedSwipes(): List<Swipe>
    suspend fun deleteSwipes(swipes: List<Swipe>)
}