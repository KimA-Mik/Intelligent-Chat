package ru.kima.intelligentchat.domain.chat.useCase.inChat

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class DeleteMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(chatId: Long, messageId: Long) {
        val messages = messageRepository.subscribeToChatMessages(chatId).first()
        if (messages.isEmpty()) return

        var msgIndex = -1
        for (i in messages.indices) {
            if (messages[i].messageId == messageId) {
                msgIndex = i
                break
            }
        }

        if (msgIndex < 0) return
        val resultedList = mutableListOf(
            messages[msgIndex].copy(deleted = true)
        )

        for (i in msgIndex + 1..messages.lastIndex) {
            val cur = messages[i]
            resultedList.add(
                cur.copy(index = cur.index - 1)
            )
        }

        messageRepository.updateMessages(resultedList)
    }
}