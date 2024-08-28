package com.compose.data.datasource.notification

import com.compose.data.remote.FcmApi
import com.compose.domain.entities.NotificationMessage
import com.compose.domain.utils.EventLogger
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    private val fcmApi: FcmApi,
    private val eventLogger: EventLogger
) : NotificationDataSource {
    override suspend fun sendNotification(notification: NotificationMessage) {
        fcmApi.sendNotification(notification)
    }

    override fun logEvent(eventName: String, params: Map<String, String>) {
        eventLogger.logEvent(eventName, params)
    }
}