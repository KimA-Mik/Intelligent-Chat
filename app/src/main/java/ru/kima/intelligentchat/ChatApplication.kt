package ru.kima.intelligentchat

import android.app.Application
import android.app.UiModeManager
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
    lateinit var uiManager: UiModeManager

    override fun onCreate() {
        super.onCreate()
        uiManager = getSystemService(UI_MODE_SERVICE) as UiModeManager

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


    companion object {
        const val MESSAGING_SERVICE_ID = 69
    }
}