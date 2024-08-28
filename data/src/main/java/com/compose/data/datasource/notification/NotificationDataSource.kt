package com.compose.data.datasource.notification

import com.compose.domain.entities.NotificationMessage

interface NotificationDataSource {
    suspend fun sendNotification(notification: NotificationMessage)
    fun logEvent(eventName: String, params: Map<String, String> = emptyMap())

}