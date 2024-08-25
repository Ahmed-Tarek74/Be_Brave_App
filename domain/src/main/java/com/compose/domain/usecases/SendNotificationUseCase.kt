package com.compose.domain.usecases

import com.compose.domain.entities.Message
import com.compose.domain.entities.Notification
import com.compose.domain.entities.NotificationData
import com.compose.domain.entities.NotificationMessage
import com.compose.domain.entities.User
import com.compose.domain.repos.DeviceTokenRepository
import com.compose.domain.repos.NotificationRepository

class SendNotificationUseCase(
    private val notificationRepository: NotificationRepository,
    private val deviceTokenRepository: DeviceTokenRepository
) {
    suspend operator fun invoke(sender: User, message: Message) {
        val token = deviceTokenRepository.getDeviceToken(message.receiverId)
        val notification = createNotification(sender, message, token)
        notificationRepository.sendNotification(notification)
    }
}

private fun createNotification(
    sender: User,
    message: Message,
    token: String
): NotificationMessage {
    val data = mapOf(
        "senderUsername" to sender.username,
        "senderImage" to sender.profilePictureUrl,
        "timestamp" to message.timestamp.toString()
    )

    val notificationData = NotificationData(
        data = data,
        token = token,
        notification = Notification(
            title = "Message from ${sender.username}",
            body = message.message
        )
    )
    return NotificationMessage(notificationData)
}
