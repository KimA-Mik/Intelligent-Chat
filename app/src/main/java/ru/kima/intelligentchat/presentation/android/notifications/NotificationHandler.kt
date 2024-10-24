package ru.kima.intelligentchat.presentation.android.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import ru.kima.intelligentchat.R

class NotificationHandler(context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initializeNotificationChannels(context)
        }
    }

    fun getCharacterTypingNotification(
        context: Context,
        characterName: String,
        characterImage: Bitmap? = null
    ): Notification {
        val title = context.getString(R.string.awaiting_for_message_notification, characterName)
        val icon = characterImage?.let {
            IconCompat.createWithBitmap(it)
        }

        val builder = NotificationCompat
            .Builder(context, AWAITING_FOR_MESSAGE_CHANNEL_ID)
            .setContentTitle(title)

        icon?.let { builder.setSmallIcon(icon) }

        return builder.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeNotificationChannels(context: Context) {
        val name = context.getString(R.string.awaiting_for_message_notification_channel_name)
        val description =
            context.getString(R.string.awaiting_for_message_notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(AWAITING_FOR_MESSAGE_CHANNEL_ID, name, importance)
        channel.description = description
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val AWAITING_FOR_MESSAGE_CHANNEL_ID = "awaiting_for_message_channel_id"
    }
}