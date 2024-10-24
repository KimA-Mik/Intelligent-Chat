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
import ru.kima.intelligentchat.core.common.API_TYPE
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.useCase.inChat.CreateMessageUseCase
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStatus
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStrategy
import ru.kima.intelligentchat.domain.messaging.generation.strategies.HordeGenerationStrategy
import ru.kima.intelligentchat.domain.messaging.generation.strategies.KoboldAiGenerationStrategy
import ru.kima.intelligentchat.domain.messaging.model.MessagingIndicator
import ru.kima.intelligentchat.domain.messaging.useCase.LoadMessagingConfigUseCase
import ru.kima.intelligentchat.domain.messaging.useCase.LoadMessagingDataUseCase
import ru.kima.intelligentchat.domain.tokenizer.LlamaTokenizer

class MessagingService : Service(), KoinComponent {
    private val strategies: Map<API_TYPE, GenerationStrategy>

    init {
        val hordeStrategy: HordeGenerationStrategy by inject()
        val koboldStrategy: KoboldAiGenerationStrategy by inject()
        strategies = mapOf(
            API_TYPE.HORDE to hordeStrategy,
            API_TYPE.KOBOLD_AI to koboldStrategy
        )
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private var currentGenerationId: String? = null

    private val _status = MutableStateFlow<MessagingIndicator>(MessagingIndicator.None)
    private val _binder = MessagingServiceBinder()

    private val loadMessagingData: LoadMessagingDataUseCase by inject()
    private val loadMessagingConfig: LoadMessagingConfigUseCase by inject()
    private val createMessage: CreateMessageUseCase by inject()

    private val tokenizer: LlamaTokenizer by inject()

    inner class MessagingServiceBinder : Binder() {
        val status = _status.asStateFlow()
    }

    override fun onBind(intent: Intent?): IBinder {
        return _binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            stopSelf()
            return START_NOT_STICKY
        }

        val chatId = intent.getLongExtra(CHAT_ID_EXTRA, 0L)
        val personaId = intent.getLongExtra(PERSONA_ID_EXTRA, 0L)
        val apiType = intent.getStringExtra(API_TYPE_EXTRA)?.let {
            API_TYPE.fromString(it)
        }
        val senderType = intent.getStringExtra(SENDER_TYPE_EXTRA)?.let {
            SenderType.fromString(it)
        }

        if (chatId == 0L || personaId == 0L || apiType == null || senderType == null) {
            stopSelf()
            return START_NOT_STICKY
        }

        runForeground(chatId, personaId, apiType, senderType)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun runForeground(
        chatId: Long,
        personaId: Long,
        apiType: API_TYPE,
        senderType: SenderType
    ) {
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
            runForegroundAsync(chatId, personaId, apiType, senderType)
        }
    }

    private suspend fun runForegroundAsync(
        chatId: Long,
        personaId: Long,
        apiType: API_TYPE,
        senderType: SenderType
    ) {
        val data = loadMessagingData(chatId, personaId, senderType)
        if (data !is LoadMessagingDataUseCase.Result.Success) {
            Log.e(TAG, "Failed to load messaging data: $data")
            return
        }

        val notificationBuilder = NotificationCompat.Builder(
            this@MessagingService,
            ChatApplication.CHAT_MESSAGE_NOTIFICATIONS_CHANNEL_ID
        )
            .setContentTitle(
                getString(
                    R.string.awaiting_for_message_notification,
                    data.sender.name
                )
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        data.sender.photo?.let {
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
            startForeground(ChatApplication.MESSAGING_SERVICE_ID, notification)
        }

        val strategy = strategies[apiType]
        if (strategy == null) {
            stopSelf()
            return
        }

        val generationRequest = loadMessagingConfig(
            apiType = apiType,
            senderType = senderType,
            personaName = data.persona.name,
            cardName = data.card.name
        ).constructGenerationRequest(
            persona = data.persona,
            card = data.card,
            chat = data.fullChat,
            generateFor = senderType,
            tokenizer = tokenizer
        )

        val senderId = when (senderType) {
            SenderType.Character -> data.card.id
            SenderType.Persona -> personaId
        }

        var resultedMessage: String? = null
        strategy.generation(generationRequest).collect { generationStatus ->
            Log.d(TAG, generationStatus.toString())
            when (generationStatus) {
                is GenerationStatus.Done -> {
                    _status.value = MessagingIndicator.None
                    currentGenerationId = null
                    resultedMessage = generationStatus.result
                }

                //TODO: handle errors
                is GenerationStatus.Error -> {
                    _status.value = MessagingIndicator.None
                    currentGenerationId = null
                }

                GenerationStatus.Generating -> {
                    _status.value = MessagingIndicator.Generating
                }

                is GenerationStatus.GeneratingWithProgress -> {
                    _status.value =
                        MessagingIndicator.DeterminedGenerating(generationStatus.progression)
                }

                GenerationStatus.Pending -> {
                    _status.value = MessagingIndicator.Pending
                }

                is GenerationStatus.Started -> {
                    currentGenerationId = generationStatus.generationId
                }
            }
        }
        resultedMessage?.let { msg -> createMessage(chatId, senderType, senderId, msg) }
        stopSelf()
    }


    companion object {
        private const val TAG = "MessagingService"
        fun getLaunchIntent(
            context: Context,
            chatId: Long,
            personaId: Long,
            apiType: API_TYPE,
            senderType: SenderType
        ): Intent {
            val intent = Intent(context, MessagingService::class.java)
            intent.putExtra(CHAT_ID_EXTRA, chatId)
            intent.putExtra(PERSONA_ID_EXTRA, personaId)
            intent.putExtra(API_TYPE_EXTRA, apiType.toString())
            intent.putExtra(SENDER_TYPE_EXTRA, senderType.toString())
            return intent
        }

        fun getBinder(binder: IBinder?): MessagingServiceBinder? {
            if (binder is MessagingServiceBinder) return binder
            return null
        }

        private const val CHAT_ID_EXTRA = "chat_id"
        private const val PERSONA_ID_EXTRA = "persona_id"
        private const val API_TYPE_EXTRA = "api_type"
        private const val SENDER_TYPE_EXTRA = "sender_type"
    }
}