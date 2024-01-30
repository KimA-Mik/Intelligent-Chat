package ru.kima.intelligentchat

import android.app.Application
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.kima.intelligentchat.core.preferences.PreferencesHandler
import ru.kima.intelligentchat.di.data
import ru.kima.intelligentchat.di.domain
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.CardDetailsViewModel
import ru.kima.intelligentchat.presentation.characterCard.charactersList.CharactersListViewModel
import ru.kima.intelligentchat.presentation.common.image.ImagePicker
import ru.kima.intelligentchat.presentation.personas.details.PersonaDetailsViewModel
import ru.kima.intelligentchat.presentation.personas.list.PersonasListViewModel
import ru.kima.intelligentchat.presentation.showImage.ShowImageViewModel


class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val core = module {
            single { PreferencesHandler(this@ChatApplication) }
        }

        val presentation = module {
            single { ImagePicker(this@ChatApplication) }
            viewModelOf(::CharactersListViewModel)
            viewModelOf(::CardDetailsViewModel)
            viewModelOf(::ShowImageViewModel)
            viewModelOf(::PersonasListViewModel)
            viewModelOf(::PersonaDetailsViewModel)
        }

        startKoin {
            androidLogger()
            modules(
                core,
                data(this@ChatApplication),
                domain(this@ChatApplication),
                presentation
            )
        }
    }
}