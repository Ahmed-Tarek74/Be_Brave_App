package com.compose.data.datasource.notification

import com.compose.data.remote.FcmApi
import com.compose.domain.entities.NotificationMessage
import javax.inject.Inject

class NotificationDataSource @Inject constructor( private val fcmApi: FcmApi) :INotificationDataSource{
    override suspend fun sendNotification(notification: NotificationMessage) {
        fcmApi.sendNotification(notification)
    }
}