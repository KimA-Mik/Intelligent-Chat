package ru.kima.intelligentchat.presentation.ui

import android.net.Uri

sealed interface ApplicationEvent {
    data class NotificationNavigation(val deeplink: Uri, val notificationId: Int) : ApplicationEvent
}