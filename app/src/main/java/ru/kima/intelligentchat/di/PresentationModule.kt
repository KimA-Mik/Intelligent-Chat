package ru.kima.intelligentchat.di

import android.content.Context
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.CardDetailsViewModel
import ru.kima.intelligentchat.presentation.characterCard.charactersList.CharactersListViewModel
import ru.kima.intelligentchat.presentation.common.image.ImagePicker
import ru.kima.intelligentchat.presentation.connection.overview.ConnectionOverviewViewModel
import ru.kima.intelligentchat.presentation.personas.details.PersonaDetailsViewModel
import ru.kima.intelligentchat.presentation.personas.list.PersonasListViewModel
import ru.kima.intelligentchat.presentation.showImage.ShowImageViewModel

fun presentation(context: Context) = module {
    single { ImagePicker(context) }

    viewModelOf(::CharactersListViewModel)
    viewModelOf(::CardDetailsViewModel)

    viewModelOf(::ShowImageViewModel)

    viewModelOf(::PersonasListViewModel)
    viewModelOf(::PersonaDetailsViewModel)

    viewModelOf(::ConnectionOverviewViewModel)
}