package ru.kima.intelligentchat.presentation.android.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.domain.common.errors.GenerationError

class NotificationHandler(
    private val context: Context
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                awaitingForMessageNotificationChannel(),
                generationErrorNotificationChannel(),
                chatMessageNotificationChannel()
            )
            notificationManager.createNotificationChannels(channels)
        }
    }

    fun getCharacterTypingNotification(
        characterName: String,
        characterImage: Bitmap? = null
    ): Notification {
        val title = context.getString(R.string.awaiting_for_message_notification, characterName)
        val builder = defaultBuilder(AWAITING_FOR_MESSAGE_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)

        characterImage?.let { builder.setLargeIcon(it) }
        return builder.build()
    }

    fun notifyGenerationError(
        senderId: Int,
        characterName: String,
        characterImage: Bitmap? = null,
        error: GenerationError
    ) {

        val title =
            context.getString(R.string.generation_error_notification_channel_title, characterName)
        val builder = defaultBuilder(GENERATION_ERROR_NOTIFICATIONS_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(error.getDescription(context))

        characterImage?.let { builder.setLargeIcon(it) }
        notificationManager.notify(senderId, builder.build())
    }

    fun notifyNewMessage(
        senderId: Int,
        characterName: String,
        characterImage: Bitmap? = null,
    ) {
        val title =
            context.getString(R.string.new_chat_message_notification, characterName)
        val builder = defaultBuilder(NEW_CHAT_MESSAGE_NOTIFICATIONS_CHANNEL_ID)
            .setContentTitle(title)

        characterImage?.let { builder.setLargeIcon(it) }
        notificationManager.notify(senderId, builder.build())
    }

    private fun defaultBuilder(channelId: String) =
        NotificationCompat
            .Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun awaitingForMessageNotificationChannel(): NotificationChannel {
        val name = context.getString(R.string.awaiting_for_message_notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel =
            NotificationChannel(AWAITING_FOR_MESSAGE_NOTIFICATION_CHANNEL_ID, name, importance)
        channel.description =
            context.getString(R.string.awaiting_for_message_notification_channel_description)
        return channel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generationErrorNotificationChannel(): NotificationChannel {
        val name = context.getString(R.string.generation_error_notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(GENERATION_ERROR_NOTIFICATIONS_CHANNEL_ID, name, importance)
        channel.description =
            context.getString(R.string.generation_error_notification_channel_description)
        return channel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun chatMessageNotificationChannel(): NotificationChannel {
        val name = context.getString(R.string.new_chat_message_notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(NEW_CHAT_MESSAGE_NOTIFICATIONS_CHANNEL_ID, name, importance)
        channel.description =
            context.getString(R.string.new_chat_message_notification_channel_description)
        return channel
    }

    companion object {
        private const val AWAITING_FOR_MESSAGE_NOTIFICATION_CHANNEL_ID =
            "awaiting_for_message_notification"
        private const val NEW_CHAT_MESSAGE_NOTIFICATIONS_CHANNEL_ID =
            "new_chat_message_notifications"
        private const val GENERATION_ERROR_NOTIFICATIONS_CHANNEL_ID =
            "generation_error_notifications"
    }
}