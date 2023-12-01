package ru.kima.intelligentchat

import android.app.Application
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.kima.intelligentchat.di.data
import ru.kima.intelligentchat.di.domain
import ru.kima.intelligentchat.presentation.cardDetails.CardDetailsViewModel
import ru.kima.intelligentchat.presentation.charactersList.CharactersListViewModel
import ru.kima.intelligentchat.presentation.common.image.ImagePicker


class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val presentation = module {
            single { ImagePicker(this@ChatApplication) }
            viewModelOf(::CharactersListViewModel)
            viewModelOf(::CardDetailsViewModel)
        }

        startKoin {
            androidLogger()
            modules(
                data(this@ChatApplication),
                domain(this@ChatApplication),
                presentation
            )
        }
    }
}