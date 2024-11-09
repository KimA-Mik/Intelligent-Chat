package ru.kima.intelligentchat.domain.messaging.generation.savingResult

import ru.kima.intelligentchat.domain.messaging.model.Sender

interface SavingStrategy {
    suspend fun save(text: String, sender: Sender)
}