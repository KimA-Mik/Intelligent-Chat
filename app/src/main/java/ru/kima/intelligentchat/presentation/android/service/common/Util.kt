package ru.kima.intelligentchat.presentation.android.service.common

import android.app.ActivityManager
import android.app.Service
import android.content.Context

@Suppress("DEPRECATION") // Deprecated for third party Services.
inline fun <reified T : Service> Context.isServiceRunning() =
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == T::class.java.name }