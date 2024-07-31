package com.compose.domain.repos

import com.compose.domain.entities.NotificationMessage
import retrofit2.Response

interface NotificationRepository {

    suspend fun sendNotification(notification: NotificationMessage): Response<Unit>
}