package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Swipe

interface SwipeRepository {
    fun subscribeSwipesForMessages(messagesId: List<Long>): Flow<List<Swipe>>
}