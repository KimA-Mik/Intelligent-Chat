package ru.kima.intelligentchat

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.kima.intelligentchat.di.data
import ru.kima.intelligentchat.di.domain
import ru.kima.intelligentchat.di.presentation
import ru.kima.intelligentchat.domain.common.useCase.CleanUpUseCase
import ru.kima.intelligentchat.presentation.service.horde.HordeConfigService

class ChatApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private lateinit var hordeConfigService: HordeConfigService
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()

        startKoin {
            androidLogger()
            androidContext(this@ChatApplication)
            modules(
                data(),
                domain(),
                presentation()
            )
        }

        hordeConfigService = get<HordeConfigService>()

        val cleanUp = get<CleanUpUseCase>()
        applicationScope.launch {
            cleanUp()
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.awaiting_for_message_notification_channel_name)
            val descriptionText =
                getString(R.string.awaiting_for_message_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel =
                NotificationChannel(CHAT_MESSAGE_NOTIFICATIONS_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    companion object {
        const val MESSAGING_SERVICE_ID = 69
        const val CHAT_MESSAGE_NOTIFICATIONS_CHANNEL_ID = "chat_message_notifications"
    }
}