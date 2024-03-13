package ru.kima.intelligentchat.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.kima.intelligentchat.core.preferences.appPreferences.PreferencesHandler
import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler

fun core() = module {
    singleOf(::PreferencesHandler)
    singleOf(::HordeStateHandler)
}