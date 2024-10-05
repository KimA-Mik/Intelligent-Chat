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
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.core.common.API_TYPE
import ru.kima.intelligentchat.core.preferences.appPreferences.PreferencesHandler
import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler
import ru.kima.intelligentchat.domain.messaging.model.MessagingStatus
import ru.kima.intelligentchat.domain.messaging.repositoty.MessagingRepository
import ru.kima.intelligentchat.presentation.android.service.common.isServiceRunning
import ru.kima.intelligentchat.presentation.android.service.messaging.MessagingService

private const val TAG = "MessagingRepositoryImpl"

class MessagingRepositoryImpl(
    private val context: Context,
    private val hordeStateHandler: HordeStateHandler,
    preferencesHandler: PreferencesHandler,
) : MessagingRepository {
    private val preferences = preferencesHandler.data
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)
    private val _messagingStatus = MutableStateFlow<MessagingStatus>(MessagingStatus.Available)

    private var _binder: MessagingService.MessagingServiceBinder? = null
    private var binderSubscriptionJob: Job? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            _binder = MessagingService.getBinder(service)
            _binder?.let { binder ->
                binderSubscriptionJob?.cancel()
                binderSubscriptionJob = coroutineScope.launch {
                    binder.status.collect {
                        _messagingStatus.value = it
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

    override fun messagingStatus(): Flow<MessagingStatus> = _messagingStatus

    override fun sendMessage() {
        TODO("Not yet implemented")
    }

    override fun cancelMessage() {
        if (_messagingStatus.value != MessagingStatus.Available) {
            TODO("Not yet implemented")
        }
    }

    private suspend fun initialize() {
        if (context.isServiceRunning<MessagingService>()) {
            bindService()
            return
        }

        val currentApi = preferences.last().selectedApiType
        when (currentApi) {
            API_TYPE.HORDE -> initHorde()
            API_TYPE.KOBOLD_AI -> {}
        }
    }

    private suspend fun initHorde() {
        val hordeGenerationId = hordeStateHandler.data.last().generationId
            ?: return

        Log.d(TAG, "Uncompleted Horde generation id: $hordeGenerationId")
        //TODO: Handle restoring generation after incomplete shutdown
    }

    private fun bindService() = context.bindService(
        Intent(context, MessagingService::class.java),
        serviceConnection, 0
    )
}