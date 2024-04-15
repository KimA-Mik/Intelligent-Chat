package ru.kima.intelligentchat.data.chat.types.sender

import androidx.room.TypeConverter

class SenderTypeConverter {
    @TypeConverter
    fun senderToInt(sender: SenderType): Int = sender.ordinal

    @TypeConverter
    fun intToSender(ordinal: Int): SenderType = SenderType.entries[ordinal]
}