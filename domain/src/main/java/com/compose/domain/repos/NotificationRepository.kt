package com.compose.domain.repos

import com.compose.domain.entities.NotificationMessage

interface NotificationRepository {

    suspend fun sendNotification(notification: NotificationMessage)
}