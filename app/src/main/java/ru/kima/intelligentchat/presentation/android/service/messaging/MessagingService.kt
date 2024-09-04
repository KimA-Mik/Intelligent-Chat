package ru.kima.intelligentchat.presentation.android.service.messaging

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class MessagingService : Service(), KoinComponent {
    sealed interface MessageStatus {
        data object Done : MessageStatus
        data object Pending : MessageStatus
        data object Generating : MessageStatus
        data class GeneratingWithProgress(val progress: Float) : MessageStatus
    }

    inner class MessagingServiceBinder : Binder() {
        val status = _status.asStateFlow()
    }

    private val _status = MutableStateFlow(MessageStatus.Done)
    private val _binder = MessagingServiceBinder()

    override fun onBind(intent: Intent?): IBinder {
        return _binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_NOT_STICKY

        val chatId = intent.getLongExtra(CHAT_ID_EXTRA, 0L)
        val personaId = intent.getLongExtra(PERSONA_ID_EXTRA, 0L)
        if (chatId == 0L || personaId == 0L) return START_NOT_STICKY

        return START_NOT_STICKY
    }

    companion object {
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