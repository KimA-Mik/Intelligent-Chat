package ru.kima.intelligentchat.data.chat.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.chat.entities.SwipeEntity
import ru.kima.intelligentchat.data.chat.mappers.toEntity
import ru.kima.intelligentchat.data.chat.mappers.toSwipe
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.domain.chat.model.Swipe
import ru.kima.intelligentchat.domain.chat.repository.SwipeRepository

class SwipeRepositoryImpl(
    wrapper: DatabaseWrapper
) : SwipeRepository {
    private val chatDao = wrapper.database.chatDao()

    override fun subscribeSwipesForMessages(messagesId: List<Long>): Flow<List<Swipe>> {
        return chatDao
            .swipesForMessages(messagesId)
            .map { it.map(SwipeEntity::toSwipe) }
    }

    override fun subscribeSwipesForChart(chatId: Long): Flow<List<Swipe>> {
        return chatDao
            .swipesForChat(chatId)
            .map { it.map(SwipeEntity::toSwipe) }
    }

    override suspend fun getSwipe(swipeId: Long): Swipe {
        return chatDao.getSwipe(swipeId).toSwipe()
    }

    override suspend fun deleteSwipesForMessage(messageId: Long): Boolean {
        return chatDao.deleteSwipesForMessage(messageId) > 0
    }

    override suspend fun deleteSwipesForMessages(messageIds: List<Long>): Boolean {
        return chatDao.deleteSwipesForMessages(messageIds) > 0
    }

    override suspend fun createSwipe(messageId: Long, text: String): Long {
        val swipe = SwipeEntity(
            swipeId = 0,
            messageId = messageId,
            text = text
        )

        return chatDao.insertSwipe(swipe)
    }

    override suspend fun updateSwipe(swipe: Swipe) {
        chatDao.updateSwipe(swipe.toEntity())
    }
}