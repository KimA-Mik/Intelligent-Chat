package ru.kima.intelligentchat.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.kima.intelligentchat.ChatApplication
import ru.kima.intelligentchat.presentation.android.notifications.NotificationHandler
import ru.kima.intelligentchat.presentation.android.preferences.appAppearance.AppAppearanceStore
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.CardDetailsViewModel
import ru.kima.intelligentchat.presentation.characterCard.charactersList.CharactersListViewModel
import ru.kima.intelligentchat.presentation.chat.cardChatList.CardChatListViewModel
import ru.kima.intelligentchat.presentation.chat.chatScreen.ChatScreenViewModel
import ru.kima.intelligentchat.presentation.common.image.ImagePicker
import ru.kima.intelligentchat.presentation.connection.overview.ConnectionOverviewViewModel
import ru.kima.intelligentchat.presentation.connection.presets.horde.edit.HordePresetEditViewModel
import ru.kima.intelligentchat.presentation.personas.details.PersonaDetailsViewModel
import ru.kima.intelligentchat.presentation.personas.list.PersonasListViewModel
import ru.kima.intelligentchat.presentation.service.horde.HordeConfigService
import ru.kima.intelligentchat.presentation.settings.root.SettingsRootViewModel
import ru.kima.intelligentchat.presentation.showImage.ShowImageViewModel

fun presentation() = module {
    singleOf(::ImagePicker)
    singleOf(::AppAppearanceStore) { createdAtStart() }

    single { (androidContext() as ChatApplication).uiManager }

    singleOf(::NotificationHandler)

    viewModelOf(::CharactersListViewModel)
    viewModelOf(::CardDetailsViewModel)

    viewModelOf(::ShowImageViewModel)

    viewModelOf(::PersonasListViewModel)
    viewModelOf(::PersonaDetailsViewModel)

    viewModelOf(::ConnectionOverviewViewModel)
    viewModelOf(::HordePresetEditViewModel)

    viewModelOf(::ChatScreenViewModel)
    viewModelOf(::CardChatListViewModel)

    singleOf(::HordeConfigService)

    viewModelOf(::SettingsRootViewModel)
}