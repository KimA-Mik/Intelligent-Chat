package ru.kima.intelligentchat.presentation.android.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import ru.kima.intelligentchat.domain.preferences.Preference

class ICPreference<T>(
    initialValue: T,
    private val flow: Flow<T>,
    private val setter: suspend (T) -> Unit,
) : Preference<T> {
    private var _value: T = initialValue
    override fun currentValue() = _value
    override fun subscribe() = flow.onEach { _value = it }
    override suspend fun set(value: T) = setter(value)
}