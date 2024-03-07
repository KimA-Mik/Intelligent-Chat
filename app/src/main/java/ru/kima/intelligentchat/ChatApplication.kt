package ru.kima.intelligentchat

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.kima.intelligentchat.di.core
import ru.kima.intelligentchat.di.data
import ru.kima.intelligentchat.di.domain
import ru.kima.intelligentchat.di.presentation

class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ChatApplication)
            modules(
                core(),
                data(),
                domain(),
                presentation()
            )
        }
    }
}