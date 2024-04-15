package ru.kima.intelligentchat.data.chat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import ru.kima.intelligentchat.data.chat.entities.SwipeEntity

@Dao
interface SwipeDao {
    @Insert
    suspend fun insertSwipe(swipeEntity: SwipeEntity): Long

    @Update
    suspend fun updateSwipe(swipeEntity: SwipeEntity)
}