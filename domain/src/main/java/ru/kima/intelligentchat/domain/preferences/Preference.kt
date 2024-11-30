package ru.kima.intelligentchat.domain.preferences

import kotlinx.coroutines.flow.Flow

interface Preference<T> {
    fun currentValue(): T
    fun subscribe(): Flow<T>
    suspend fun set(value: T)
}