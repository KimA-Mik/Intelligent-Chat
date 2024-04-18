package ru.kima.intelligentchat.data.chat.types.sender

import androidx.room.TypeConverter

class SenderTypeConverter {
    @TypeConverter
    fun senderToInt(sender: SenderTypeDto): Int = sender.ordinal

    @TypeConverter
    fun intToSender(ordinal: Int): SenderTypeDto = SenderTypeDto.entries[ordinal]
}