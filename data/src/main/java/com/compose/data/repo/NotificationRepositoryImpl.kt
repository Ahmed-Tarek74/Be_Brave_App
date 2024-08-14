package com.compose.data.repo

import com.compose.data.remote.FcmApi
import com.compose.domain.entities.NotificationMessage
import com.compose.domain.repos.NotificationRepository
class NotificationRepositoryImpl(
    private val fcmApi: FcmApi
) : NotificationRepository {

    override suspend fun sendNotification(notification: NotificationMessage) {
        try {
            fcmApi.sendNotification(notification)
        } catch (e: Exception) {
            throw Exception("Failed to send notification")
        }
    }
}