package com.compose.data.repo

import com.compose.data.remote.FcmApi
import com.compose.domain.entities.NotificationMessage
import com.compose.domain.repos.NotificationRepository
import retrofit2.Response

class NotificationRepositoryImpl(
    private val fcmApi: FcmApi
) : NotificationRepository {

    override suspend fun sendNotification(notification: NotificationMessage): Response<Unit> {
        return fcmApi.sendNotification(notification)
    }
}