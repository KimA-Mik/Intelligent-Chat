package ru.kima.intelligentchat.presentation.android.implementation.messaging.repositoty

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.core.common.API_TYPE
import ru.kima.intelligentchat.core.preferences.appPreferences.PreferencesHandler
import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.messaging.model.MessagingIndicator
import ru.kima.intelligentchat.domain.messaging.repositoty.MessagingRepository
import ru.kima.intelligentchat.presentation.android.service.common.isServiceRunning
import ru.kima.intelligentchat.presentation.android.service.messaging.MessagingService

private const val TAG = "MessagingRepositoryImpl"

class MessagingRepositoryImpl(
    private val context: Context,
    private val hordeStateHandler: HordeStateHandler,
    private val preferencesHandler: PreferencesHandler,
) : MessagingRepository {
    private val preferences = preferencesHandler.data
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)
    private val _generationStatus = MutableStateFlow<MessagingIndicator>(MessagingIndicator.None)

    private var _binder: MessagingService.MessagingServiceBinder? = null
    private var binderSubscriptionJob: Job? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            _binder = MessagingService.getBinder(service)
            _binder?.let { binder ->
                binderSubscriptionJob?.cancel()
                binderSubscriptionJob = coroutineScope.launch {
                    binder.status.collect {
                        _generationStatus.value = it
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            _binder = null
            binderSubscriptionJob?.cancel()
            binderSubscriptionJob = null
        }
    }

    init {
        coroutineScope.launch {
            initialize()
        }
    }

    override fun messagingStatus(): Flow<MessagingIndicator> = _generationStatus

    override fun initiateGeneration(chatId: Long, personaId: Long, senderType: SenderType) {
        coroutineScope.launch {
            if (context.isServiceRunning<MessagingService>()) {
                return@launch
            }

            val api = preferences.first().selectedApiType
            val intent =
                MessagingService.getLaunchIntent(context, chatId, personaId, api, senderType)
            context.startService(intent)
            bindService()
        }
    }

    override suspend fun cancelGeneration() {
        _binder?.cancelGeneration()
    }

    private suspend fun initialize() {
        if (context.isServiceRunning<MessagingService>()) {
            bindService()
            return
        }

        preferencesHandler.updateGenerationPending(false)
        val currentApi = preferences.first().selectedApiType
        when (currentApi) {
            API_TYPE.HORDE -> initHorde()
            API_TYPE.KOBOLD_AI -> {}
        }
    }

    private suspend fun initHorde() {
        val hordeGenerationId = hordeStateHandler.data.first().generationId
            ?: return

        Log.d(TAG, "Uncompleted Horde generation id: $hordeGenerationId")
        //TODO: Handle restoring generation after incomplete shutdown
    }

    private fun bindService() = context.bindService(
        Intent(context, MessagingService::class.java),
        serviceConnection, 0
    )
}