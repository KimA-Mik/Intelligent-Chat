package ru.kima.intelligentchat.presentation.settings.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kima.intelligentchat.domain.preferences.Preference


@Composable
fun <T> Preference<T>.collectAsState(): State<T> {
    val flow = remember(this) { subscribe() }
    return flow.collectAsStateWithLifecycle(currentValue())
}