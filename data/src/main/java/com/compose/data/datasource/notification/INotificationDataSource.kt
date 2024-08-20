package com.compose.data.datasource.notification

import com.compose.domain.entities.NotificationMessage

interface INotificationDataSource {
    suspend fun sendNotification(notification: NotificationMessage)
}