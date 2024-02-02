package ru.kima.intelligentchat

import android.app.Application
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.kima.intelligentchat.core.preferences.PreferencesHandler
import ru.kima.intelligentchat.di.data
import ru.kima.intelligentchat.di.domain
import ru.kima.intelligentchat.di.presentation


class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val core = module {
            single { PreferencesHandler(this@ChatApplication) }
        }

        startKoin {
            androidLogger()
            modules(
                core,
                data(this@ChatApplication),
                domain(this@ChatApplication),
                presentation(this@ChatApplication)
            )
        }
    }
}