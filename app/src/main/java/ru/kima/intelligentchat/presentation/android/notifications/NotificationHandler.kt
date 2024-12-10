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

class NotificationHandler(
    private val context: Context
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(awaitingForMessageNotificationChannel())
            notificationManager.createNotificationChannel(generationErrorNotificationChannel())
        }
    }

    fun getCharacterTypingNotification(
        characterName: String,
        characterImage: Bitmap? = null
    ): Notification {
        val title = context.getString(R.string.awaiting_for_message_notification, characterName)
        val builder = NotificationCompat
            .Builder(context, AWAITING_FOR_MESSAGE_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        characterImage?.let { builder.setLargeIcon(it) }
        return builder.build()
    }

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

    companion object {
        private const val AWAITING_FOR_MESSAGE_NOTIFICATION_CHANNEL_ID =
            "awaiting_for_message_notification_channel"
        private const val CHAT_MESSAGE_NOTIFICATIONS_CHANNEL_ID = "chat_message_notifications"
        private const val GENERATION_ERROR_NOTIFICATIONS_CHANNEL_ID =
            "generation_error_notifications"
    }
}