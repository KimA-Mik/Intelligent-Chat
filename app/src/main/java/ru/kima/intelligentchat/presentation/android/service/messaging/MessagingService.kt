package ru.kima.intelligentchat.presentation.android.service.messaging

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kima.intelligentchat.ChatApplication
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.domain.messaging.useCase.LoadMessagingDataUseCase

class MessagingService : Service(), KoinComponent {
    sealed interface MessageStatus {
        data object Done : MessageStatus
        data object Pending : MessageStatus
        data object Generating : MessageStatus
        data class GeneratingWithProgress(val progress: Float) : MessageStatus
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private val _status = MutableStateFlow(MessageStatus.Done)
    private val _binder = MessagingServiceBinder()

    private val loadMessagingData: LoadMessagingDataUseCase by inject()

    inner class MessagingServiceBinder : Binder() {
        val status = _status.asStateFlow()
    }

    override fun onBind(intent: Intent?): IBinder {
        return _binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_NOT_STICKY

        val chatId = intent.getLongExtra(CHAT_ID_EXTRA, 0L)
        val personaId = intent.getLongExtra(PERSONA_ID_EXTRA, 0L)
        if (chatId == 0L || personaId == 0L) return START_NOT_STICKY

        runForeground(chatId, personaId)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun runForeground(chatId: Long, personaId: Long) {
        val handler = CoroutineExceptionHandler { _, exception ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && exception is ForegroundServiceStartNotAllowedException) {
                Log.e(
                    TAG,
                    "App not in a valid state to start foreground service (e.g. started from bg)"
                )
            } else {
                Log.e(TAG, "Failed to start foreground service", exception)
            }

            this@MessagingService.stopSelf()
        }

        scope.launch(Dispatchers.Main + handler) {
            runForegroundAsync(chatId, personaId)
        }
    }

    private suspend fun runForegroundAsync(chatId: Long, personaId: Long) {
        val data = loadMessagingData(chatId, personaId)
        if (data !is LoadMessagingDataUseCase.Result.Success) {
            Log.e(TAG, "Failed to load messaging data: $data")
            return
        }

        val sender = data.sender
        val senderName = when (sender) {
            is LoadMessagingDataUseCase.LastSender.CharacterSender -> sender.card.name
            is LoadMessagingDataUseCase.LastSender.PersonaSender -> sender.persona.name
        }
        val senderImage = when (sender) {
            is LoadMessagingDataUseCase.LastSender.CharacterSender -> sender.card.photoBytes
            is LoadMessagingDataUseCase.LastSender.PersonaSender -> sender.image.bitmap
        }

        val notificationBuilder = NotificationCompat.Builder(
            this@MessagingService,
            ChatApplication.CHAT_MESSAGE_NOTIFICATIONS_CHANNEL_ID
        )
            .setContentTitle(getString(R.string.awaiting_for_message_notification, senderName))
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        senderImage?.let {
            notificationBuilder.setLargeIcon(it)
        }

        val notification = notificationBuilder.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceCompat.startForeground(
                this@MessagingService,
                ChatApplication.MESSAGING_SERVICE_ID,
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING
                else
                    0
            )
        } else {
            startForeground(100, notification)
        }
    }

    companion object {
        private const val TAG = "MessagingService"
        fun getLaunchIntent(context: Context, chatId: Long, personaId: Long): Intent {
            val intent = Intent(context, this::class.java)
            intent.putExtra(CHAT_ID_EXTRA, chatId)
            intent.putExtra(PERSONA_ID_EXTRA, personaId)
            return intent
        }

        fun getBinder(binder: IBinder?): MessagingServiceBinder? {
            if (binder is MessagingServiceBinder) return binder
            return null
        }

        private const val CHAT_ID_EXTRA = "chat_id"
        private const val PERSONA_ID_EXTRA = "persona_id"
    }
}