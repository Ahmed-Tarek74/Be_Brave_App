package com.compose.data.repo

import com.compose.data.datasource.notification.NotificationDataSource
import com.compose.domain.entities.NotificationMessage
import com.compose.domain.repos.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource
) : NotificationRepository {

    override suspend fun sendNotification(notification: NotificationMessage) {
        try {
            notificationDataSource.sendNotification(notification)
        } catch (e: Exception) {
            throw Exception("Failed to send notification")
        }
    }
}